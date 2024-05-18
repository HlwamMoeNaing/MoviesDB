package com.hmn.moviesdb.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hmn.moviesdb.ui.screens.login.LoginViewModel

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.homeNavGraph(navController: NavHostController,loginViewModel: LoginViewModel) {
    navigation(startDestination = Routes.HomeScreen.name, route = AppNavGraph.HOME) {
        composable(route = Routes.HomeScreen.name) {
            AppScaffold(){
                loginViewModel.clearLoginInfo()
                navController.navigate(AppNavGraph.AUTH){
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
        }

        authGraph(navController,loginViewModel)
    }
}