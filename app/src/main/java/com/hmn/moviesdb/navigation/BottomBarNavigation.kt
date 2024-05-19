package com.hmn.moviesdb.navigation

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hmn.moviesdb.ui.screens.detail.DetailViewModel
import com.hmn.moviesdb.ui.screens.favourite.FavouriteScreen
import com.hmn.moviesdb.ui.screens.home.HomeScreen
import com.hmn.moviesdb.ui.screens.home.HomeViewModel
import com.hmn.moviesdb.ui.screens.search.SearchScreen
import com.hmn.moviesdb.ui.screens.search.SearchViewModel
import com.hmn.moviesdb.ui.screens.view_all.ViewAllViewModel
import com.hmn.moviesdb.utils.exitApp

@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomBarNavigation(
    navController: NavHostController,
    detailViewModel: DetailViewModel,
    paddingValues: PaddingValues,
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val viewAllViewModel: ViewAllViewModel = hiltViewModel()
    val searchViewModel: SearchViewModel = hiltViewModel()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Routes.HomeScreen.name) {
        composable(Routes.HomeScreen.name) {
            HomeScreen(homeViewModel = homeViewModel, navController = navController)
            BackHandler {
                exitApp(context)
            }
        }

        composable(Routes.FavouriteScreen.name) {
            FavouriteScreen(viewModel = homeViewModel, navController = navController)
        }

        composable(Routes.SearchScreen.name) {
            SearchScreen(searchViewModel,navController,paddingValues)
        }

        detailNavGraph(detailViewModel = detailViewModel, navController = navController, viewAllViewModel = viewAllViewModel)
    }

}