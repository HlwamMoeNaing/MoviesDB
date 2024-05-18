package com.hmn.data.model.resp


sealed class ApiResponse<out T> {
    data object Loading : ApiResponse<Nothing>()
    data object Default : ApiResponse<Nothing>()
    data class Success<T>(val data: T?) : ApiResponse<T>()
    data class ApiError(val message: String) : ApiResponse<Nothing>()
    data class AuthorizationError(val message: String) : ApiResponse<Nothing>()
    data class NetworkError(val message: String) : ApiResponse<Nothing>()
    data class ServerConnectionError(val message: String) : ApiResponse<Nothing>()
    data class TimeoutError(val message: String) : ApiResponse<Nothing>()
}
