package com.hmn.moviesdb.ui.screens.splash

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.hmn.data.movies_data.local.MyDataStore
import com.hmn.data.repo.MovieRepository
import com.hmn.data.utils.NetworkUtil
import com.hmn.moviesdb.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    context: Application,
    private val movieRepository: MovieRepository,
    private val networkUtil: NetworkUtil,
    private val myDataStore: MyDataStore
) : BaseViewModel(context,networkUtil) {

    private val _splashUiState = MutableStateFlow(SplashUiState())
    val splashUiState = _splashUiState.asStateFlow()

    val userName = mutableStateOf("")


    init {
        getDataStore()
        checkNetwork()
    }

    fun getDataStore(){
        viewModelScope.launch {
            userName.value = myDataStore.getEmail.first()
        }
    }

    private fun checkNetwork() {
        viewModelScope.launch {
            _splashUiState.update {
                it.copy(
                    isLoading = true
                )
            }
            delay(500)
            val isConnected = networkUtil.isNetworkConnected()
            val email = myDataStore.getEmail.first()
            val isLogin = myDataStore.getLoginStatus.first()

            if (isConnected) {
                if (email.isNotEmpty() && isLogin) {
                    _splashUiState.update {
                        it.copy(
                            shouldHome = true, shouldLogin = false
                        )
                    }
                } else {
                    _splashUiState.update {
                        it.copy(
                            shouldHome = false, shouldLogin = true
                        )
                    }
                }
            } else {
                if (movieRepository.checkMoviesExist()) {
                    if (email.isNotEmpty() && isLogin) {
                        _splashUiState.update {
                            it.copy(
                                shouldHome = true
                            )
                        }
                    } else {
                        _splashUiState.update {
                            it.copy(
                                shouldHome = false, shouldLogin = true
                            )
                        }
                    }
                } else {

                    _splashUiState.update {
                        it.copy(
                            shouldErrorDialog = true
                        )
                    }
                    showToast("You are not connected with internet and no data exist")
                }


            }

            _splashUiState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    fun onEvent(event: SplashUiEvent) {
        when (event) {
            is SplashUiEvent.NavigateToSearch -> navigateToSearch()
            is SplashUiEvent.Navigate -> navigate(event.route)
        }
    }
}

data class SplashUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val shouldLogin: Boolean = false,
    val shouldHome: Boolean = false,
    val shouldErrorDialog: Boolean = false,
    val isCacheDataExist: Boolean = false
)

sealed class SplashUiEvent {
    data object NavigateToSearch : SplashUiEvent()

    data class Navigate(val route: String) : SplashUiEvent()
}

