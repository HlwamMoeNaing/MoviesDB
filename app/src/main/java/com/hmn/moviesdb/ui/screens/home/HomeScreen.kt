package com.hmn.moviesdb.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.hmn.moviesdb.core.BaseScreen

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    navController: NavController
) {

    HomeScreenContent(viewModel = homeViewModel)
//    BaseScreen(viewModel = homeViewModel, navController = navController) {
//        HomeScreenContent(viewModel = homeViewModel)
//    }

}

@Composable
fun HomeScreenContent(
    viewModel: HomeViewModel
) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Red), contentAlignment = Alignment.Center) {
        Text(text = "Home")
    }
}