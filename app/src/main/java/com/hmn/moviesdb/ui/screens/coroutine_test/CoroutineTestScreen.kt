package com.hmn.moviesdb.ui.screens.coroutine_test

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.hmn.moviesdb.core.BaseScreen

@Composable
fun CoroutineScreen(
    modifier: Modifier = Modifier,
    viewModel: CoroutineViewModel = hiltViewModel(),
) {

    val resultState by viewModel.resultState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    BaseScreen(viewModel = viewModel, navController = navController) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (resultState.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = resultState.result)
            }
        }
    }

}