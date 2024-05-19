package com.hmn.moviesdb.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.hmn.moviesdb.ui.screens.YouTubePlayer
import com.hmn.moviesdb.ui.screens.detail.DetailScreen
import com.hmn.moviesdb.ui.screens.detail.DetailViewModel
import com.hmn.moviesdb.ui.screens.view_all.ViewAllScreen
import com.hmn.moviesdb.ui.screens.view_all.ViewAllViewModel

fun NavGraphBuilder.detailNavGraph(
    navController: NavHostController,
    detailViewModel: DetailViewModel,
    viewAllViewModel: ViewAllViewModel
) {
    navigation(
        route = AppNavGraph.DETAIL,
        startDestination = Routes.DetailScreen.name

    ) {
        composable(
            route = Routes.DetailScreen.name + "/{detailId}",
            arguments = listOf(navArgument("detailId") { type = NavType.IntType })
        ) { backStackEntry ->
            val detailId = backStackEntry.arguments?.getInt("detailId") ?: 0
            DetailScreen(
                detailViewModel = detailViewModel,
                navController = navController,
                detailId = detailId
            ){
                navController.navigateUp()
            }
        }

        composable(route = Routes.ViewAllScreen.name + "/{category}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType }
            )) {
            ViewAllScreen(
                viewModel = viewAllViewModel,
                category = it.arguments?.getString("category") ?: "",
                navController = navController
            )
        }
//
//        composable(route = Routes.PlayerScreen.name + "/{videoUrl}",
//            arguments = listOf(
//                navArgument("videoUrl") { type = NavType.StringType }
//            )) {
//            val videoUrl = it.arguments?.getString("videoUrl") ?: ""
//            YouTubePlayer(youtubeVideoId = videoUrl)
//        }

    }
}