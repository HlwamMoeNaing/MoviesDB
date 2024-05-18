package com.hmn.moviesdb.ui.screens.splash

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hmn.moviesdb.core.BaseScreen
import com.hmn.moviesdb.core.ConnectionLifeCycle

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel,
    navController: NavController,
    onHome: () -> Unit,
    onLogin: () -> Unit
) {

    ConnectionLifeCycle(
        check = {
            viewModel.registerNetworkCallback()
        },
    ) {
        viewModel.unregisterNetworkCallback()
    }

    BaseScreen(viewModel = viewModel, navController = navController) {
        SplashScreenContent(modifier = modifier, viewModel, onLogin = {onLogin()}, onHome = {onHome()})

    }

}

@Composable
fun SplashScreenContent(
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel,
    onLogin: () -> Unit,
    onHome: () -> Unit
) {

    val splashUiState by viewModel.splashUiState.collectAsStateWithLifecycle()
    val isError = splashUiState.isError
    val errorMessage = splashUiState.errorMessage
    val shouldErrorDialog = splashUiState.shouldErrorDialog
    val isLoading = splashUiState.isLoading
    val shouldLogin = splashUiState.shouldLogin
    val shouldHome = splashUiState.shouldHome

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Yellow), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(50.dp))


        }
    }

    if (shouldErrorDialog) {

    }

    if (shouldHome) {
        onHome()
    }
    if (shouldLogin) {
        onLogin()
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Yellow), contentAlignment = Alignment.Center) {
        Text(text = "Splash Screen")
    }


}