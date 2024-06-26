package com.dicoding.armand.storyapp.utils

sealed class NetworkResult<T>(
    val resultData: T? = null,
    val resultMessage: String? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
}