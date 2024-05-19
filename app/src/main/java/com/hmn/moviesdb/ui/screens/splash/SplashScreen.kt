package com.hmn.moviesdb.ui.screens.splash

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hmn.moviesdb.R
import com.hmn.moviesdb.components.WarningDialog
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


    if (shouldErrorDialog && !errorMessage.isNullOrEmpty()) {
        WarningDialog(
            attemptAccount = 0,
            message = errorMessage,
            onProcess = {

            },
            onDismiss = { }
        )
    }

    if (shouldHome) {
        onHome()
    }
    if (shouldLogin) {
        onLogin()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0E0E0)), // Use an appropriate background color
        contentAlignment = Alignment.Center
    ) {
        BouncingImage(
            modifier = Modifier,
            imageResId = R.drawable.app_icon_foreground
        )
    }


}


@Composable
fun BouncingImage(
    modifier: Modifier = Modifier,
    imageResId: Int
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Image(
        painter = painterResource(id = imageResId),
        contentDescription = "Bouncing Image",
        modifier = modifier
            .size(100.dp)
            .scale(scale)
    )
}