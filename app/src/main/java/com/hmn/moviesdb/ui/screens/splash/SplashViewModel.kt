package com.hmn.moviesdb.ui.screens.splash

import android.app.Application
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.hmn.data.domain.MovieRepo
import com.hmn.data.domain.MoviesDbRepo
import com.hmn.data.model.resp.AppDataResult
import com.hmn.data.movies_data.local.MyDataStore
import com.hmn.data.utils.NetworkUtil
import com.hmn.moviesdb.core.BaseViewModel
import com.hmn.moviesdb.core.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

//
//@HiltViewModel
//class SplashViewModel @Inject constructor(
//    context: Application,
//    private val moviesRepo: MovieRepo,
//    private val movieDbRepo: MoviesDbRepo,
//    private val networkUtil: NetworkUtil
//) : BaseViewModel(context) {
//    private val _splashUiState = MutableStateFlow(SplashUiState())
//    val splashUiState = _splashUiState.asStateFlow()
//
//
//    private var remoteBaseJob: Job? = null
//    private var nowPlayJob: Job? = null
//    private var topRateJob: Job? = null
//    private var popularJob: Job? = null
//
//
//
//
//    fun checkNetwork() {
//        val isConnected = networkUtil.isNetworkConnected()
//        Log.d("SpScreen", "VM checkNetwork: isConnected $isConnected")
//       _splashUiState.update {
//           it.copy(
//               isNetworkAvailable = true
//           )
//       }
//
//        if (isConnected) {
//            savePopularMovies()
//        } else {
//            checkOldData()
//        }
//    }
//
//    private fun savePopularMovies() {
//        viewModelScope.launch {
//            if (remoteBaseJob?.isActive == true) {
//                remoteBaseJob?.cancel()
//            }
//            if (nowPlayJob?.isActive == true) {
//                nowPlayJob?.cancel()
//            }
//
//            if (topRateJob?.isActive == true) {
//                topRateJob?.cancel()
//            }
//            if (popularJob?.isActive == true) {
//                popularJob?.cancel()
//            }
//            remoteBaseJob = viewModelScope.launch(Dispatchers.IO) {
//                try {
//                    _splashUiState.update {
//                        it.copy(isLoading = true)
//                    }
//                    coroutineScope {
//                        nowPlayJob = launch {
//                            try {
//                                when (val response = moviesRepo.getAndSaveNowPlayingMovies()) {
//                                    is AppDataResult.Error -> {
//                                        val errorMessage = response.exception.message
//                                        _splashUiState.update {
//                                            it.copy(
//                                                isLoading = false,
//                                                isError = true,
//                                                errorMessage = errorMessage
//                                            )
//                                        }
//                                    }
//
//                                    else -> Unit
//                                }
//                            } catch (e: Exception) {
//                                _splashUiState.update {
//                                    it.copy(
//                                        isError = true,
//                                        errorMessage = e.message
//                                    )
//                                }
//                            }
//                        }
//
//
//                        topRateJob = launch {
//                            try {
//                                when (val response = moviesRepo.getAndSaveTopRatedMovies()) {
//                                    is AppDataResult.Error -> {
//                                        val errorMessage = response.exception.message
//                                        _splashUiState.update {
//                                            it.copy(
//                                                isLoading = false,
//                                                isError = true,
//                                                errorMessage = errorMessage
//                                            )
//                                        }
//                                    }
//
//                                    else -> Unit
//                                }
//                            } catch (e: Exception) {
//                                _splashUiState.update {
//                                    it.copy(
//                                        isLoading = false,
//                                        isError = true,
//                                        errorMessage = e.message
//                                    )
//                                }
//                            }
//                        }
//
//
//                        popularJob = launch {
//                            try {
//                                when (val response = moviesRepo.getAndSavePopularMovies()) {
//                                    is AppDataResult.Error -> {
//                                        val errorMessage = response.exception.message
//                                        _splashUiState.update {
//                                            it.copy(
//                                                isLoading = false,
//                                                isError = true,
//                                                errorMessage = errorMessage
//                                            )
//                                        }
//                                    }
//
//                                    else -> Unit
//                                }
//                            } catch (e: Exception) {
//                                _splashUiState.update {
//                                    it.copy(
//                                        isLoading = false,
//                                        isError = true,
//                                        errorMessage = e.message
//                                    )
//                                }
//                            }
//                        }
//
//                        _splashUiState.update {
//                            it.copy(
//                                isLoading = false,
//                                isReadyData = true
//                            )
//                        }
//
//                        popularJob?.join()
//                        nowPlayJob?.join()
//                        topRateJob?.join()
//                    }
//
//
//                } catch (e: Exception) {
//                    _splashUiState.value =
//                        SplashUiState(isLoading = false, isError = true, errorMessage = e.message)
//                } finally {
//                    _splashUiState.update {
//                        it.copy(isLoading = false)
//                    }
//                }
//            }
//        }
//    }
//
//    private fun checkOldData() {
//        viewModelScope.launch {
//            try {
//                movieDbRepo.getAllMovies().collectLatest {
//                    if (it.isNotEmpty()) {
//                        _splashUiState.update { spUiState ->
//                            spUiState.copy(isCacheDataExist = true)
//                        }
//                    } else {
//                        _splashUiState.update { spUiState ->
//                            spUiState.copy(isCacheDataExist = false)
//                        }
//
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    init {
//        checkNetwork()
//        savePopularMovies()
//        checkOldData()
//    }
//}
//
//data class SplashUiState(
//    val isLoading: Boolean = false,
//    val isError: Boolean = false,
//    val errorMessage: String? = null,
//    val shouldLogin: Boolean = false,
//    val isReadyData: Boolean = false,
//
//    val isCacheDataExist: Boolean = false,
//
//    val isNetworkAvailable: Boolean = false,
//
//        val isLee:Boolean = false
//
//)


@HiltViewModel
class SplashViewModel @Inject constructor(
    context: Application,
    private val movieDbRepo: MoviesDbRepo,
    private val networkUtil: NetworkUtil,
    val myDataStore: MyDataStore
) : BaseViewModel(context) {

    private val _splashUiState = MutableStateFlow(SplashUiState())
    val splashUiState = _splashUiState.asStateFlow()

    init {
        checkNetwork()
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
            val userName = myDataStore.getUserName.first()
            val isLogin = myDataStore.getLoginStatus.first()

            if (isConnected) {
                if (userName.isNotEmpty() && isLogin) {
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
                checkOldData()
                if (_splashUiState.value.isCacheDataExist) {
                    if (userName.isNotEmpty() && isLogin) {
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

    private fun checkOldData() {
        viewModelScope.launch {
            try {
                movieDbRepo.getAllMovies().collectLatest { movies ->
                    _splashUiState.update { spUiState ->
                        spUiState.copy(isCacheDataExist = movies.isNotEmpty())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _splashUiState.update { spUiState ->
                    spUiState.copy(isError = true, errorMessage = e.message)
                }
            }
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

