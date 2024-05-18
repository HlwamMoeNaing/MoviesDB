package com.hmn.data.model.resp

sealed class AppDataResult<out T> {
    data class Success<out T>(val data: T) : AppDataResult<T>()
    data class Error(val exception: Exception) : AppDataResult<Nothing>()
    data object Loading : AppDataResult<Nothing>()
    data object Default : AppDataResult<Nothing>()
}