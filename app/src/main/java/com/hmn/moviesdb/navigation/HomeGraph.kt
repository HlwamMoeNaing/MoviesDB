package com.hmn.moviesdb.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {
    navigation(startDestination = Routes.HomeScreen.name, route = AppNavGraph.HOME) {
        composable(route = Routes.HomeScreen.name) {
            AppScaffold()
        }
    }
}