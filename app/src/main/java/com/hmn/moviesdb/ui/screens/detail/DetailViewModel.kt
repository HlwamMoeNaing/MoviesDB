package com.hmn.moviesdb.ui.screens.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hmn.data.model.resp.MovieVo
import com.hmn.data.repo.MovieRepository
import com.hmn.data.utils.NetworkUtil
import com.hmn.moviesdb.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val favStateErrorMessage: String? = null
)

sealed class DetailUiEvent {
    data class OnFavourite(val movieId: Int, val isFav: Boolean) : DetailUiEvent()
}
