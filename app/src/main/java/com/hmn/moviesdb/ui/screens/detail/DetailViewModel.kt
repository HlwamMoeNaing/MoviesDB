package com.hmn.moviesdb.ui.screens.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hmn.data.model.ResourceState
import com.hmn.data.model.resp.MovieVo
import com.hmn.data.repo.MovieRepository
import com.hmn.data.utils.NetworkUtil
import com.hmn.moviesdb.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    context: Application,
    private val networkUtil: NetworkUtil
) : BaseViewModel(context, networkUtil) {
    private val _detailUiState = MutableStateFlow(DetailUiState())
    val detailUiState = _detailUiState.asStateFlow()

    private val _videoUiState = MutableStateFlow(VideoState())
    val videoUiState = _videoUiState.asStateFlow()

    val isNetwork = MutableStateFlow(false)

    init {
        checkNetwork()
    }

    fun checkNetwork() {
        viewModelScope.launch {
            isNetwork.value = networkUtil.isNetworkConnected()
        }
    }

    fun getMovieById(id: Int) {
        viewModelScope.launch {
            try {
                _detailUiState.update {
                    it.copy(isLoading = true)
                }
                val data = repository.getMovieById(id)
                _detailUiState.update {
                    it.copy(isLoading = false, movie = data, isFavorite = data.isFavourite)
                }
            } catch (e: Exception) {
                _detailUiState.update {
                    it.copy(isLoading = false, error = e.message)
                }
            }
        }
    }

    private fun updateFavStatus(movieId: Int, isFav: Boolean) {
        viewModelScope.launch {
            try {
                repository.updateFavStatus(movieId, isFav)
                _detailUiState.update { it.copy(isFavorite = isFav) }
            } catch (e: Exception) {
                _detailUiState.update {
                    it.copy(isFavErrorOccure = true, favStateErrorMessage = e.message)
                }
            }
        }
    }

    fun getVideoInfoWithId(id: Int, isInternetAvailable: Boolean) {
        Log.d("@VdKey", "getVideoInfoWithId: Start")
        viewModelScope.launch {
            if (!isInternetAvailable) {
                Log.d("@VdKey", "getVideoInfoWithId: inside not internet")
                _videoUiState.update {
                    it.copy(
                        videoLoading = false,
                        videoError = true,
                        videoErrorMessage = "Network Unavailable"
                    )
                }
                return@launch
            }

            Log.d("@VdKey", "getVideoInfoWithId: Start Loading")
            _videoUiState.update {
                it.copy(
                    videoLoading = true,
                    videoError = false,
                    videoErrorMessage = null,
                    videoKey = ""
                )
            }

            try {
                delay(500)
                when (val response = repository.getVideoWithId(id)) {
                    is ResourceState.Error -> {
                        Log.d("@VdKey", "getVideoInfoWithId: Error")
                        _videoUiState.update {
                            it.copy(
                                videoLoading = false,
                                videoError = true,
                                videoErrorMessage = response.error,
                                videoKey = null
                            )
                        }
                    }
                    is ResourceState.Loading -> {
                        Log.d("@VdKey", "getVideoInfoWithId: loading Sate Rsp")
                        // This state is already handled before the request
                    }
                    is ResourceState.Success -> {
                        Log.d("@VdKey", "getVideoInfoWithId: success")
                        val data = response.data
                        val result = data.results.firstOrNull()
                        _videoUiState.update {
                            it.copy(
                                videoLoading = false,
                                videoError = false,
                                videoErrorMessage = null,
                                videoKey = result?.key
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("@VdKey", "getVideoInfoWithId: Exception")
                _videoUiState.update {
                    it.copy(
                        videoLoading = false,
                        videoError = true,
                        videoErrorMessage = e.localizedMessage ?: "Exception Occurred",
                        videoKey = null
                    )
                }
            }
        }
    }

    fun onEvent(event: DetailUiEvent) {
        when (event) {
            is DetailUiEvent.OnFavourite -> updateFavStatus(event.movieId, event.isFav)
        }
    }
}

data class DetailUiState(
    val isLoading: Boolean = false,
    val movie: MovieVo? = null,
    val error: String? = null,
    val isFavorite: Boolean = false,
    val isFavErrorOccure: Boolean = false,
    val favStateErrorMessage: String? = null,
)

data class VideoState(
    val videoLoading: Boolean = false,
    val videoError: Boolean = false,
    val videoKey: String? = null,
    val videoErrorMessage: String? = null
)

sealed class DetailUiEvent {
    data class OnFavourite(val movieId: Int, val isFav: Boolean) : DetailUiEvent()
}
