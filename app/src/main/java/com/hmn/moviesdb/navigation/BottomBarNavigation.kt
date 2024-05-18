package com.hmn.moviesdb.navigation

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hmn.moviesdb.ui.screens.favourite.FavouriteScreen
import com.hmn.moviesdb.ui.screens.home.HomeScreen
import com.hmn.moviesdb.ui.screens.home.HomeViewModel
import com.hmn.moviesdb.ui.screens.search.SearchScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomBarNavigation(
    navController: NavHostController,
    paddingValues: PaddingValues,
) {
    val homeViewModel:HomeViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = Routes.HomeScreen.name) {
        composable(Routes.HomeScreen.name) {
            HomeScreen(homeViewModel = homeViewModel, navController = navController)
            BackHandler {
                //  ExitApp(context)
            }
        }

        composable(Routes.FavouriteScreen.name) {
            FavouriteScreen()
        }

        composable(Routes.SearchScreen.name) {
            SearchScreen()
        }

        detailNavGraph(navController)
    }
}