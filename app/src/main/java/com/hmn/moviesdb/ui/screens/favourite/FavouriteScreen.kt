package com.hmn.moviesdb.ui.screens.favourite

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hmn.moviesdb.core.BaseScreen
import com.hmn.moviesdb.ui.screens.detail.DetailAppBar
import com.hmn.moviesdb.ui.screens.home.HomeUiEvent
import com.hmn.moviesdb.ui.screens.home.HomeViewModel
import com.hmn.moviesdb.ui.screens.view_all.ViewAllEvent
import com.hmn.moviesdb.ui.screens.view_all.ViewAllItem

@Composable
fun FavouriteScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    navController: NavController
) {
    BaseScreen(viewModel = viewModel, navController = navController) {
        FavouriteContent(viewModel = viewModel)
    }

}

@Composable
fun FavouriteContent(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getFavoriteMovies()
    }

    val favUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val favMovies = favUiState.fetchFavouriteMovies
    val errorMessage = favUiState.errorMessage

    if (errorMessage.isNullOrEmpty()) {
        Column(modifier = Modifier.fillMaxSize()) {
            DetailAppBar(
                tittle = "Favourites",
                isFav = false,
                onBackClick = { },
                onFavClickL = { },
                tintColor = Color.Transparent
            )
            LazyColumn {
                items(favMovies, key = { it.id }) { movie ->
                    ViewAllItem(movies = movie, goToDetailScreen = {
                        viewModel.onEvent(
                            HomeUiEvent.NavigateToDetail(it)
                        )
                    })
                }
            }
        }
    }

}