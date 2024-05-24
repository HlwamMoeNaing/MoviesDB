package com.hmn.moviesdb.ui.screens

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.hmn.data.model.resp.MovieVo
import com.hmn.data.repo.MovieRepository
import com.hmn.data.utils.MovieCategory
import com.hmn.data.utils.NetworkUtil
import com.hmn.moviesdb.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestingViewModel @Inject constructor(
    context: Application,
    private val movieRepository: MovieRepository,
    private val networkUtil: NetworkUtil
) : BaseViewModel(context, networkUtil) {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    init {
        fetchMovies()
        getFavoriteMovies()

    }

    private suspend fun savingMovies() {
        viewModelScope.launch {
            val nowPlayingDefferabale = async { fetchAndSaveMovies(MovieCategory.NOW_PLAYING) }
            val topRatedDefferabale = async { fetchAndSaveMovies(MovieCategory.TOP_RATED) }
            val popularDefferabale = async { fetchAndSaveMovies(MovieCategory.POPULAR) }
            nowPlayingDefferabale.await()
            topRatedDefferabale.await()
            popularDefferabale.await()

        }
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            _homeUiState.update { it.copy(isLoading = true) }
            try {
                val savingDefferabale = async { savingMovies() }
                val getNowPlayingDeffer =
                    async { movieRepository.getMoviesByCategory(MovieCategory.NOW_PLAYING.name) }
                val getTopRatedDeffer =
                    async { movieRepository.getMoviesByCategory(MovieCategory.TOP_RATED.name) }
                val getPopularDeffer =
                    async { movieRepository.getMoviesByCategory(MovieCategory.POPULAR.name) }
                savingDefferabale.await()
                val nowPlayingMovies = getNowPlayingDeffer.await()
                val topRatedMovies = getTopRatedDeffer.await()
                val popularMovies = getPopularDeffer.await()
                _homeUiState.update {
                    it.copy(
                        isLoading = false,
                        nowPlayingMovies = nowPlayingMovies,
                        topRatedMovies = topRatedMovies,
                        popularMovies = popularMovies
                    )
                }
            } catch (e: Exception) {
                handleFetchMoviesError(e)
            }
        }
    }

    private suspend fun fetchAndSaveMovies(category: MovieCategory) {
        if (networkUtil.isNetworkConnected()) {
            movieRepository.fetchAndSaveMovies(category)
        } else {
            handleFetchMoviesError(Exception("No network connection"))
        }
    }

    private fun handleFetchMoviesError(e: Exception) {
        viewModelScope.launch {
            try {
                if (movieRepository.checkMoviesExist()) {
                    movieRepository.getAllMovies().collect { movies ->
                        val nowPlayDefferabale =
                            async { movies.filter { it.category == MovieCategory.NOW_PLAYING.name } }
                        val topRatedDefferabale =
                            async { movies.filter { it.category == MovieCategory.TOP_RATED.name } }
                        val popularDefferabale =
                            async { movies.filter { it.category == MovieCategory.POPULAR.name } }
                        val nowPlayingMovies = nowPlayDefferabale.await()
                        val topRatedMovies = topRatedDefferabale.await()
                        val popularMovies = popularDefferabale.await()

                        _homeUiState.update {
                            it.copy(
                                isLoading = false,
                                nowPlayingMovies = nowPlayingMovies,
                                topRatedMovies = topRatedMovies,
                                popularMovies = popularMovies
                            )
                        }
                    }
                } else {
                    _homeUiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = e.localizedMessage
                        )
                    }
                }
            } catch (dbError: Exception) {
                _homeUiState.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = dbError.localizedMessage
                    )
                }
            }
        }
    }

    fun getFavoriteMovies() {
        viewModelScope.launch {
            try {
                val favMovies = movieRepository.getFavouriteMovies()
                _homeUiState.update {
                    it.copy(fetchFavouriteMovies = favMovies)
                }
            } catch (e: Exception) {
                _homeUiState.update {
                    it.copy(errorMessageForFavourite = e.localizedMessage)
                }
            }
        }
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.NavigateToDetail -> navigateToDetail(event.id)
            is HomeUiEvent.NavigateToViewAll -> navigateToViewAllWithCategory(event.category)
        }

    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val nowPlayingMovies: List<MovieVo> = emptyList(),
    val topRatedMovies: List<MovieVo> = emptyList(),
    val popularMovies: List<MovieVo> = emptyList(),

    val fetchFavouriteMovies: List<MovieVo> = emptyList(),
    val errorMessageForFavourite: String? = null
)

sealed class HomeUiEvent {

    data class NavigateToDetail(val id: Int) : HomeUiEvent()

    data class NavigateToViewAll(val category: String) : HomeUiEvent()

}


/**
 *  private fun fetchMovies() {
 *         viewModelScope.launch {
 *             _homeUiState.update { it.copy(isLoading = true) }
 *
 *             try {
 * //                fetchAndSaveMovies(MovieCategory.NOW_PLAYING)
 * //                fetchAndSaveMovies(MovieCategory.TOP_RATED)
 * //                fetchAndSaveMovies(MovieCategory.POPULAR)
 *                 val savingDefferabale = async { savingMovies() }
 *
 *                 val getNowPlayingDeffer =
 *                     async { movieRepository.getMoviesByCategory(MovieCategory.NOW_PLAYING.name) }
 *                 val getTopRatedDeffer =
 *                     async { movieRepository.getMoviesByCategory(MovieCategory.TOP_RATED.name) }
 *                 val getPopularDeffer =
 *                     async { movieRepository.getMoviesByCategory(MovieCategory.POPULAR.name) }
 *                 savingDefferabale.await()
 *                 val nowPlayingMovies = getNowPlayingDeffer.await()
 *                 val topRatedMovies = getTopRatedDeffer.await()
 *                 val popularMovies = getPopularDeffer.await()
 *
 *
 * //                val nowPlayingMovies =
 * //                    movieRepository.getMoviesByCategory(MovieCategory.NOW_PLAYING.name)
 * //                val topRatedMovies =
 * //                    movieRepository.getMoviesByCategory(MovieCategory.TOP_RATED.name)
 * //                val popularMovies = movieRepository.getMoviesByCategory(MovieCategory.POPULAR.name)
 *
 *                 _homeUiState.update {
 *                     it.copy(
 *                         isLoading = false,
 *                         nowPlayingMovies = nowPlayingMovies,
 *                         topRatedMovies = topRatedMovies,
 *                         popularMovies = popularMovies
 *                     )
 *                 }
 *             } catch (e: Exception) {
 *                 handleFetchMoviesError(e)
 *             }
 *         }
 *     }
 */

/**
 *    private fun handleFetchMoviesError(e: Exception) {
 *         viewModelScope.launch {
 *             try {
 *                 if (movieRepository.checkMoviesExist()) {
 *                     movieRepository.getAllMovies().collect { movies ->
 *                         val nowPlayingMovies =
 *                             movies.filter { it.category == MovieCategory.NOW_PLAYING.name }
 *                         val topRatedMovies =
 *                             movies.filter { it.category == MovieCategory.TOP_RATED.name }
 *                         val popularMovies =
 *                             movies.filter { it.category == MovieCategory.POPULAR.name }
 *
 *                         _homeUiState.update {
 *                             it.copy(
 *                                 isLoading = false,
 *                                 nowPlayingMovies = nowPlayingMovies,
 *                                 topRatedMovies = topRatedMovies,
 *                                 popularMovies = popularMovies
 *                             )
 *                         }
 *                     }
 *                 } else {
 *                     _homeUiState.update {
 *                         it.copy(
 *                             isLoading = false,
 *                             isError = true,
 *                             errorMessage = e.localizedMessage
 *                         )
 *                     }
 *                 }
 *             } catch (dbError: Exception) {
 *                 _homeUiState.update {
 *                     it.copy(
 *                         isLoading = false,
 *                         isError = true,
 *                         errorMessage = dbError.localizedMessage
 *                     )
 *                 }
 *             }
 *         }
 *     }
 */