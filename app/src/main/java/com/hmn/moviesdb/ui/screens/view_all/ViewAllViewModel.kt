package com.hmn.moviesdb.ui.screens.view_all

import android.app.Application
import androidx.lifecycle.viewModelScope
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
class ViewAllViewModel @Inject constructor(
    context: Application,
    private val movieRepository: MovieRepository,
    private val networkUtil: NetworkUtil
) : BaseViewModel(context, networkUtil) {
    private val _viewAllUiState = MutableStateFlow(ViewAllUiState())
    val viewAllUiState = _viewAllUiState.asStateFlow()


    fun fetchMoviesWithCategory(category: String) {
        baseCheckNetwork()
        viewModelScope.launch {
            _viewAllUiState.value = ViewAllUiState(isLoading = true)
            delay(500)
            try {
                val movies = movieRepository.getMoviesByCategory(category)
                _viewAllUiState.update {
                    it.copy(
                        isLoading = false,
                        movies = movies
                    )
                }
            } catch (e: Exception) {
                _viewAllUiState.update {
                    it.copy(
                        isLoading = false,
                    )
                }
            }
        }

    }

    fun onEvent(event: ViewAllEvent) {
        when (event) {
            is ViewAllEvent.NavigateToDetail -> navigateToDetail(event.movieId)
            ViewAllEvent.OnBackPress -> navigateUp()
        }
    }
}

data class ViewAllUiState(
    val isLoading: Boolean = false,
    val movies: List<MovieVo> = emptyList()
)

sealed class ViewAllEvent {
    data class NavigateToDetail(val movieId: Int) : ViewAllEvent()
    data object OnBackPress : ViewAllEvent()
}