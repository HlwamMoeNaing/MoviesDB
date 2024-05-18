package com.hmn.moviesdb.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hmn.moviesdb.core.BaseScreen
import com.hmn.moviesdb.navigation.Routes

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    navController: NavController
) {
    BaseScreen(viewModel = loginViewModel, navController = navController) {
        LoginScreenContent(loginViewModel = loginViewModel)
    }
}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue), contentAlignment = Alignment.Center
    ) {
        Text(text = "Login Screen")
    }
}