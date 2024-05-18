package com.hmn.moviesdb.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hmn.moviesdb.ui.screens.detail.DetailScreen
import com.hmn.moviesdb.ui.screens.favourite.FavouriteScreen
import com.hmn.moviesdb.ui.screens.home.HomeScreen

fun NavGraphBuilder.detailNavGraph(
    navController: NavHostController,
    //splashViewModel: SplashViewModel
) {
    navigation(
        route = AppNavGraph.DETAIL,
        startDestination = Routes.DetailScreen.name

    ) {
        composable(route = Routes.DetailScreen.name) {
           DetailScreen()
        }
    }
}