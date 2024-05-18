package com.hmn.moviesdb.core

import android.content.Context
import android.content.pm.PackageInfo
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmn.moviesdb.navigation.Routes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


abstract class BaseViewModel(
    context: Context,
) : ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    private val toast = Toast.makeText(context, "", Toast.LENGTH_LONG)
    private val packageInfo: PackageInfo =
        context.packageManager.getPackageInfo(context.packageName, 0)
    private val versionName: String = packageInfo.versionName


    private val _networkState = MutableStateFlow<NetworkState>(NetworkState.Disconnected)
    val networkState get() = _networkState.asStateFlow()


    private val networkCallback = NetworkCallbackImpl(this)
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun navigate(route: String) {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.Navigate(route))
        }
    }


    fun navigateToDetail(tId: Int) {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.Navigate("${Routes.DetailScreen.name}/$tId"))
        }
    }

    fun navigateToSearch() {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.Navigate(Routes.SearchScreen.name))
        }
    }

    fun navigateUp() {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }


    fun showToast(message: String) {
        toast.setText(message)

        toast.show()
    }

    fun onNetworkChanged(isConnected: Boolean) {
        viewModelScope.launch {
            _networkState.update {
                if (isConnected) {
                    NetworkState.Connected
                } else {
                    NetworkState.Disconnected
                }
            }


        }
    }

    fun checkConnectServer() {
        if (networkState.value is NetworkState.Connected) {
//            checkServer()
        } else {

        }
    }


    fun registerNetworkCallback() {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}

sealed class UiEvent {
    data object Nothing : UiEvent()
    data object Success : UiEvent()
    data object NavigateUp : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class NavigateToDetail(val tId: Int) : UiEvent()
    data class ShowToast(val message: String) : UiEvent()
}

sealed class NetworkState {
    data object Connected : NetworkState()
    data object Disconnected : NetworkState()
}

data class NetworkInfoState(
    val isNetworkAvailable: Boolean = false,
)

