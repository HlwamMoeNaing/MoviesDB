package com.hmn.moviesdb.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hmn.moviesdb.ui.screens.login.LoginViewModel
import com.hmn.moviesdb.ui.screens.splash.SplashScreen
import com.hmn.moviesdb.ui.screens.splash.SplashViewModel


fun NavGraphBuilder.entryNavGraph(
    navController: NavHostController,
    splashViewModel: SplashViewModel,
    loginViewModel: LoginViewModel
) {
    navigation(
        route = AppNavGraph.ENTRY,
        startDestination = Routes.SplashScreen.name

    ) {
        composable(route = Routes.SplashScreen.name) {
            SplashScreen(
                navController = navController,
                viewModel = splashViewModel,
                onHome = {
                    navController.navigate(AppNavGraph.HOME) {
                        popUpTo(Routes.SplashScreen.name) {
                            inclusive = true
                        }
                    }
                },
                onLogin = {
                    navController.navigate(AppNavGraph.AUTH) {
                        popUpTo(Routes.SplashScreen.name) {
                            inclusive = true
                        }
                    }
                }
            )
        }
      authGraph(navController = navController,loginViewModel = loginViewModel)

    }
}