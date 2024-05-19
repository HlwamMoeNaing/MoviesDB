package com.hmn.moviesdb.ui.screens.login

import android.app.Application
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.lifecycle.viewModelScope
import com.hmn.data.movies_data.local.MyDataStore
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
class LoginViewModel @Inject constructor(
    context: Application,
    private val myDataStore: MyDataStore,
    private val repository: MovieRepository,
    private val networkUtil: NetworkUtil,
) : BaseViewModel(context,networkUtil) {
    private val _loginUiState = MutableStateFlow(LoginUiState())
   val loginUiState = _loginUiState.asStateFlow()


    private fun setLoginData(email: String, password: Int) {
        viewModelScope.launch {
            try {
                myDataStore.setEmail(email)
                myDataStore.setPassword(password)
                myDataStore.setLoginStatus(true)
                _loginUiState.update {
                    it.copy(isSuccess = true)
                }
               navigateToHomeGraph()
            } catch (e: Exception) {
                _loginUiState.update {
                    it.copy(isError = true, isSuccess = false, errorMessage = e.message.toString())
                }
            }
        }
    }

    fun clearLoginInfo(){
        viewModelScope.launch {
            myDataStore.setEmail("")
            myDataStore.setPassword(0)
            myDataStore.setLoginStatus(false)
        }
    }

    fun clearDatabase(){
        viewModelScope.launch {
            repository.deleteAllMovies()
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.Login -> {
                setLoginData(event.email, event.password)
            }
        }
    }
}

data class LoginUiState(
    val isError: Boolean = false,
    val errorMessage: String? = "",
    val isSuccess: Boolean = false,
    val password: String = "",
    val email: String = "",
)

sealed class LoginEvent {
    data class Login(
        val email: String,
        val password: Int
    ) : LoginEvent()

}


