package com.hmn.data.movies_data.network

import com.hmn.data.model.resp.ApiResponse
import kotlinx.coroutines.delay
import org.json.JSONException
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

object ApiResponseHandler {
    suspend fun <T> processResponse(makeApiCall: suspend () -> Response<T>): ApiResponse<T> {
        return try {
            val response = makeApiCall()
            delay(1000)
            if (response.isSuccessful) {
                ApiResponse.Success(response.body())
            } else {
                handleResponseCode(response.code())
            }
        } catch (e: SocketTimeoutException) {
            handleError(e)
        } catch (e: Exception) {
            e.printStackTrace()
            handleError(e)
        }
    }

    private fun <T> handleError(exception: Exception): ApiResponse<T> {
        return when (exception) {
            is NetworkException -> ApiResponse.NetworkError(exception.message.toString())
            is SocketTimeoutException -> ApiResponse.ApiError("The request timed out")
            is ConnectException -> ApiResponse.ServerConnectionError("Device Disconnected.")
            is IOException -> ApiResponse.ApiError(exception.message.toString())
            is JSONException -> ApiResponse.ApiError("Error parsing response")
            is HttpException -> ApiResponse.ApiError(exception.message.toString())
            is IllegalStateException -> ApiResponse.ApiError(exception.message.toString())
            else -> ApiResponse.ApiError("Unknown error occurred")
        }
    }

    private fun <T> handleResponseCode(code: Int): ApiResponse<T> {
        return when (code) {
            401 -> ApiResponse.AuthorizationError("Something Wrong in authorization")
            400 -> ApiResponse.ApiError("Bad request")
            404 -> ApiResponse.ApiError("Resource not found")
            500 -> ApiResponse.ApiError("Internal server error")
            else -> ApiResponse.ApiError("HTTP error: $code")
        }
    }
}

