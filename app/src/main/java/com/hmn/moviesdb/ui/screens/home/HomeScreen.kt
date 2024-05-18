package com.hmn.moviesdb.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hmn.data.model.resp.MovieVo
import com.hmn.data.utils.MovieCategory
import com.hmn.moviesdb.components.LoadingDialog
import com.hmn.moviesdb.components.WarningDialog
import com.hmn.moviesdb.core.BaseScreen

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    navController: NavController
) {
    BaseScreen(viewModel = homeViewModel, navController = navController) {
        HomeScreenContent(viewModel = homeViewModel)
    }

}


@Composable
fun HomeScreenContent(viewModel: HomeViewModel) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val nowPlayMovies = homeUiState.nowPlayingMovies
    val popularMovies = homeUiState.popularMovies
    val topMovies = homeUiState.topRatedMovies
    val isLoading = homeUiState.isLoading
    val isError = homeUiState.isError
    val errorMessage = homeUiState.errorMessage

    Log.d("@ldTest", "HomeScreenContent: $isLoading")
    if (isError && !errorMessage.isNullOrEmpty()) {
        WarningDialog(
            attemptAccount = 0,
            message = errorMessage,
            onProcess = {

            },
            onDismiss = { }
        )
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
    val scrollState = rememberScrollState()
    SwipeRefresh(state = swipeRefreshState,
        onRefresh = {
            // viewModel.loadStuff()
        }
    ) {
        Scaffold { padding ->

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }else{
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(
                            top = padding.calculateTopPadding() + 24.dp,
                            bottom = padding.calculateBottomPadding() + 24.dp,
                            start = 5.dp, end = 5.dp
                        )
                ) {
                    Text(
                        text = "Welcome back, Dandi!",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Book Now Playing Movie Here!",
                            style = MaterialTheme.typography.titleMedium,

                            )
                        TextButton(onClick = {
                            //navController.navigate(Routes.allMoviesScreenWithArgs(MovieCategory.NOW_PLAYING.name))
                            viewModel.onEvent(
                                HomeUiEvent.NavigateToViewAll(MovieCategory.NOW_PLAYING.name)
                            )

                        }) {
                            Text(text = "See All")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Banner(
                        nowPlaying = nowPlayMovies
                    ) {
                        // navController.navigate(Routes.detailScreenWithArgs(it))

                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp)
                    ) {
                        Text(
                            text = "Category",
                            style = MaterialTheme.typography.titleSmall,
                        )
                        TextButton(onClick = {
                            //navController.navigate(Routes.allMoviesScreenWithArgs(MovieCategory.NOW_PLAYING.name))
                        }) {
                            Text(text = "See All")
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Categories {
                        //navController.navigate(AppScreen.Favourite.routes)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp)
                    ) {
                        Text(
                            text = "Top RatedMovie",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        TextButton(onClick = {
                            viewModel.onEvent(
                                HomeUiEvent.NavigateToViewAll(MovieCategory.TOP_RATED.name)
                            )
                        }) {
                            Text(text = "See All")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TopRatedMovies(topRatedMovies = topMovies, onDetailScreen = {
                        //  navController.navigate(AppScreen.Favourite.routes)
                    }, onSeatScreen = {
                        //  navController.navigate(AppScreen.Seat.routes)
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp)
                    ) {
                        Text(
                            text = "Popular Movie",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        TextButton(onClick = {
                            viewModel.onEvent(
                                HomeUiEvent.NavigateToViewAll(MovieCategory.POPULAR.name)
                            )
                        }) {
                            Text(text = "See All")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    PopularMovies(
                        popularMovies = popularMovies,
                        onMovieClick = { id, category ->
                            viewModel.onEvent(
                                HomeUiEvent.NavigateToDetail(id)
                            )
                            // navController.navigate(Routes.detailScreenWithArgs(id))
                        })
                    Spacer(modifier = Modifier.height(25.dp))
                }
            }


        }
    }
}
