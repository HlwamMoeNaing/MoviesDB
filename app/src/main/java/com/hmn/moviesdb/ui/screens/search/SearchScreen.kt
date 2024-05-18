package com.hmn.moviesdb.ui.screens.search

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.DisposableEffect
import com.hmn.moviesdb.core.BaseScreen
import com.hmn.moviesdb.ui.screens.view_all.ViewAllItem

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navController: NavController,
    paddingValues: PaddingValues
) {
   BaseScreen(viewModel = viewModel, navController = navController) {
       SearchScreenContent(viewModel = viewModel, paddingValues = paddingValues)
   }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    viewModel: SearchViewModel,
    paddingValues: PaddingValues
) {

    DisposableEffect(key1 = Unit) {
        onDispose {
            viewModel.clearCache()
        }
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by remember {
        mutableStateOf("")
    }
    var active by remember {
        mutableStateOf(false)
    }


    Scaffold {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth(),
            query = text,
            onQueryChange = {
                text = it
                // searchViewModel.searchPlantsByKeyword(text)
                viewModel.searchMovieByTitle(text)
            },
            onSearch = {
                keyboardController?.hide()
            },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = { Text("Search by title or category") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search"
                )
            },
            trailingIcon = {
                if (active) {
                    Icon(
                        modifier = Modifier.clickable {
                            active = false
                            text = ""
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = "close"
                    )
                }
            },
            colors = SearchBarDefaults.colors(
                inputFieldColors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Red,
                    focusedContainerColor = Color.Red,
                    focusedIndicatorColor = Color.Red,
                    unfocusedIndicatorColor = Color.Red,
                    disabledIndicatorColor = Color.Red,
                )
            ),
        ) {

            SearchList(padding = it, searchViewModel = viewModel, searchText = text)
        }
    }
}

@Composable
fun SearchList(
    padding: PaddingValues,
    searchViewModel: SearchViewModel,
    searchText: String
) {
    val filteredMovie = searchViewModel.filteredMovies

    if (filteredMovie.isEmpty() && searchText.isNotBlank()) {
        // No results found
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                "No results found for \"$searchText\"",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else if (filteredMovie.isEmpty() && searchText.isBlank()) {
        // Before searching state
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Start typing to search for plants", style = MaterialTheme.typography.bodyMedium)
        }
    } else {
        // Display search results
        LazyColumn(
            modifier = Modifier.padding(8.dp),
            contentPadding = padding
        ) {
            items(filteredMovie, key = { it.id }) { movie ->
                ViewAllItem(movies = movie, goToDetailScreen = {

                    searchViewModel.onEvent(SearchUiEvent.GoToDetailScreen(it))
                })
            }
        }
    }
}