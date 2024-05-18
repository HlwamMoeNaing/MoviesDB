package com.hmn.moviesdb.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hmn.moviesdb.ui.screens.login.LoginScreen
import com.hmn.moviesdb.ui.screens.login.LoginViewModel

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel
    ) {
    navigation(
        route = AppNavGraph.AUTH,
        startDestination = Routes.LoginScreen.name

    ) {
        composable(route = Routes.LoginScreen.name) {
            LoginScreen(loginViewModel = loginViewModel,navController = navController)
        }

    }
}