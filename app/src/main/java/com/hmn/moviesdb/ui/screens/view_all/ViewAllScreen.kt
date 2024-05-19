package com.hmn.moviesdb.ui.screens.view_all

import android.util.Log
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hmn.moviesdb.core.BaseScreen
import com.hmn.moviesdb.ui.screens.detail.DetailAppBar

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ViewAllScreen(
    modifier: Modifier = Modifier,
    viewModel: ViewAllViewModel,
    category: String,
    navController: NavController
) {

    BaseScreen(viewModel = viewModel, navController = navController) {
        ViewAllContent(
            modifier = modifier,
            category = category,
            viewModel = viewModel
        )
    }


}

@Composable
fun ViewAllContent(
    modifier: Modifier = Modifier,
    category: String,
    viewModel: ViewAllViewModel
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.fetchMoviesWithCategory(category)
    }
    val viewAllUiState by viewModel.viewAllUiState.collectAsStateWithLifecycle()
    val movies = viewAllUiState.movies
    val isLoading = viewAllUiState.isLoading

    if (isLoading){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }else{
        if (movies.isNotEmpty()) {
            Column(modifier = Modifier.fillMaxSize()) {
                DetailAppBar(
                    tittle = category.toUpperCase(),
                    isFav = false,
                    onBackClick = { },
                    onFavClickL = { },
                    tintColor = Color.Transparent)
                LazyColumn {
                    items(movies, key = { it.id }) { movie ->
                        ViewAllItem(movies = movie, goToDetailScreen = {
                            Log.d("@vall", "ViewAllScreen: $it ")
                            viewModel.onEvent(ViewAllEvent.NavigateToDetail(it))
                        })
                    }
                }
            }


        }
    }


}

