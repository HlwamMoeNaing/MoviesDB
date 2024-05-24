package com.hmn.moviesdb.ui.screens.coroutine_test

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.hmn.data.utils.NetworkUtil
import com.hmn.moviesdb.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class CoroutineViewModel @Inject constructor(
    context: Application,
    networkUtil: NetworkUtil
) : BaseViewModel(context, networkUtil) {

    private val _resultState = MutableStateFlow(ResultState())
    val resultState = _resultState.asStateFlow()

    init {
        callApi()
    }


    private suspend fun firstApiCall(): String {
        delay(5000)
        return "First API call"
    }

    private suspend fun secondApiCall(): String {
        delay(2000)
        return "Second API call"
    }

    /**
     *  private fun callApi() {
     *         viewModelScope.launch {
     *             val startCurrentTimeMilliSecond = System.currentTimeMillis()
     *             val result2 = secondApiCall()
     *             val result = firstApiCall()
     *
     *             val endCurrentTimeMilliSecond = System.currentTimeMillis()
     *             val diff = endCurrentTimeMilliSecond - startCurrentTimeMilliSecond
     *             val diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(diff)
     *             Log.d("@CoroutineViewModel", "start enter callApi $result<- AND ->$result2 and time taken $diffInSeconds seconds")
     *
     *         }
     *     }
     */

    private fun callApi() {
        //with concurrent api call
        viewModelScope.launch {
            val startCurrentTimeMilliSecond = System.currentTimeMillis()
            val result1Deferred = async { firstApiCall() }
            val result2Deferred = async { secondApiCall() }
            val result1 = result2Deferred.await()
            val result2 = result1Deferred.await()
            val endCurrentTimeMilliSecond = System.currentTimeMillis()
            val diff = endCurrentTimeMilliSecond - startCurrentTimeMilliSecond
            val diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(diff)
            Log.d("@CoroutineViewModel", "start enter callApi $result1<- AND ->$result2 and time taken $diffInSeconds seconds")
        }
    }

}

data class ResultState(
    val isLoading: Boolean = false,
    val result: String = ""
)


/**
 * First Testing
 *    init {
 *        callApi()
 *    }
 *     private suspend fun firstApiCall(): String {
 *         Log.d("@CoroutineViewModel", "firstApiCalling")
 *         delay(5000)
 *         Log.d("@CoroutineViewModel", "firstApiCall finished")
 *         return "1234"
 *     }
 *
 *
 *     private  fun secondApiCall(token: String): String {
 *         Log.d("@CoroutineViewModel", "secondApiCalling")
 *         Log.d("@CoroutineViewModel", "finishApiCall finished")
 *         return "Result from API call $token"
 *     }
 *
 *     private fun callApi() {
 *         Log.d("@CoroutineViewModel", "start enter callApi")
 *         viewModelScope.launch {
 *             _resultState.update {
 *                 it.copy(
 *                     isLoading = true
 *                 )
 *             }
 *             val result = firstApiCall()
 *             val result2 = secondApiCall(result)
 *             _resultState.update {
 *                 it.copy(
 *                     isLoading = false,
 *                     result = result2
 *                 )
 *             }
 *         }
 *     }
 * */


/**
 * Second Testing ( In this version, the second api call will start after finish the first api call (sequentially ). Mean that they are not concurrently running)
 *     init {
 *         callApi()
 *     }
 *
 *
 *     private suspend fun firstApiCall(): String {
 *         delay(5000)
 *         return "First API call"
 *     }
 *
 *     private suspend fun secondApiCall(): String {
 *         delay(2000)
 *         return "Second API call"
 *     }
 *
 *     private fun callApi() {
 *         viewModelScope.launch {
 *             val startCurrentTimeMilliSecond = System.currentTimeMillis()
 *             val result2 = secondApiCall()
 *             val result = firstApiCall()
 *
 *             val endCurrentTimeMilliSecond = System.currentTimeMillis()
 *             val diff = endCurrentTimeMilliSecond - startCurrentTimeMilliSecond
 *             val diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(diff)
 *             Log.d("@CoroutineViewModel", "start enter callApi $result<- AND ->$result2 and time taken $diffInSeconds seconds")
 *
 *         }
 *     }
 *
 */
