package com.hmn.moviesdb.ui.screens.home

import android.app.Application
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import androidx.lifecycle.viewModelScope
import com.hmn.data.domain.MovieRepo
import com.hmn.data.model.resp.AppDataResult
import com.hmn.data.model.resp.MovieVo
import com.hmn.moviesdb.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    context:Application,
    private val moviesRepo: MovieRepo,
    private val dbRepo: MovieRepo
):BaseViewModel(context){
    private val _homeUiState = MutableStateFlow(MovieUiState())
    val homeUiState = _homeUiState.asStateFlow()

    private var remoteBaseJob: Job? = null
    private var nowPlayJob: Job? = null
    private var topRateJob: Job? = null
    private var popularJob: Job? = null
    private fun savePopularMovies() {
        viewModelScope.launch {
            if (remoteBaseJob?.isActive == true) remoteBaseJob?.cancel()
            if (nowPlayJob?.isActive == true) nowPlayJob?.cancel()
            if (topRateJob?.isActive == true) topRateJob?.cancel()
            if (popularJob?.isActive == true) popularJob?.cancel()

            remoteBaseJob = viewModelScope.launch(Dispatchers.IO) {
                try {
                    _homeUiState.update { it.copy(isLoading = true) }
                    coroutineScope {
                        nowPlayJob = launch { handleMovieResponse(moviesRepo::getAndSaveNowPlayingMovies) }
                        topRateJob = launch { handleMovieResponse(moviesRepo::getAndSaveTopRatedMovies) }
                        popularJob = launch { handleMovieResponse(moviesRepo::getAndSavePopularMovies) }

                        popularJob?.join()
                        nowPlayJob?.join()
                        topRateJob?.join()

                        _homeUiState.update { it.copy(isLoading = false, isReadyData = true) }
                    }
                } catch (e: Exception) {
                    _homeUiState.update { it.copy(isLoading = false, isError = true, errorMessage = e.message) }
                }
            }
        }
    }

    private suspend fun handleMovieResponse(responseFunction: suspend () -> AppDataResult<String>) {
        try {
            when (val response = responseFunction()) {
                is AppDataResult.Error -> {
                    val errorMessage = response.exception.message
                    _homeUiState.update {
                        it.copy(isLoading = false, isError = true, errorMessage = errorMessage)
                    }
                }
                else -> Unit
            }
        } catch (e: Exception) {
            _homeUiState.update { it.copy(isLoading = false, isError = true, errorMessage = e.message) }
        }
    }


}

data class MovieUiState(
    val isLoading:Boolean = false,
    val topRateMovies:MutableList<MovieVo> = mutableListOf(),
    val nowPlayMovies:MutableList<MovieVo> = mutableListOf(),
    val popularMovies:MutableList<MovieVo> = mutableListOf(),
    val isError:Boolean =false,
    val errorMessage: String? = null,
    val isReadyData:Boolean = false
)