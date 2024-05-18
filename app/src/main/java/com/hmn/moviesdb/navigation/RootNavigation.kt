package com.hmn.moviesdb.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.hmn.moviesdb.ui.screens.login.LoginViewModel
import com.hmn.moviesdb.ui.screens.splash.SplashViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootNavigation() {
    val navController = rememberNavController()
    val splashViewModel: SplashViewModel = hiltViewModel()
    val loginViewModel:LoginViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = AppNavGraph.ENTRY) {
        entryNavGraph(navController, splashViewModel = splashViewModel,loginViewModel = loginViewModel)
        homeNavGraph(navController)
    }
}

