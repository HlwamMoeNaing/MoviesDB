package com.hmn.moviesdb.core

import android.content.Context
import android.content.pm.PackageInfo
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.hmn.moviesdb.navigation.Routes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@Composable
fun BaseScreen(
    viewModel: BaseViewModel,
    navController: NavController,
    content: @Composable () -> Unit,
) {
    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle(UiEvent.Nothing)
    LaunchedEffect(key1 = uiEvent) {
        when (uiEvent) {
            is UiEvent.Navigate -> {
                val route = (uiEvent as UiEvent.Navigate).route
                navController.navigate(route)
            }

            is UiEvent.NavigateToDetail -> {
                navController.navigate("${Routes.DetailScreen.name}/${(uiEvent as UiEvent.NavigateToDetail).tId}")
            }

            is UiEvent.ShowToast -> {}

            UiEvent.NavigateUp -> {
                navController.navigateUp()
            }

            UiEvent.Nothing -> {}

            UiEvent.Success -> {}

        }

    }
    content()
}