package com.hmn.moviesdb.ui.screens.search

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.hmn.data.model.resp.MovieVo
import com.hmn.data.repo.MovieRepository
import com.hmn.data.utils.NetworkUtil
import com.hmn.moviesdb.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel  @Inject constructor(
    context: Application,
    private val movieRepository: MovieRepository,
    private val networkUtil: NetworkUtil
) : BaseViewModel(context, networkUtil) {
    val filteredMovies = mutableStateListOf<MovieVo>()
    val isLoading = MutableStateFlow(false)
    val isError = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")
    fun searchMovieByTitle(title: String) {
        val searchQuery = "%$title%"
        viewModelScope.launch {
            isLoading.value = true
            delay(500)
            try {
              movieRepository.searchMoviesByTitle(searchQuery).collect{
                    filteredMovies.clear()
                    filteredMovies.addAll(it)
              }

            } catch (e: Exception) {
                isError.value = true
                errorMessage.value = e.message.toString()
            } finally {
                isLoading.value = false
            }

        }

    }
    fun clearCache(){
        viewModelScope.launch {
            filteredMovies.clear()
        }
    }

    fun onEvent(event: SearchUiEvent) {
        when (event) {
            is SearchUiEvent.GoToDetailScreen -> {
                navigateToDetail(event.movieId)
            }
        }
    }
}

sealed class SearchUiEvent{
    data class GoToDetailScreen(val movieId: Int) : SearchUiEvent()
}