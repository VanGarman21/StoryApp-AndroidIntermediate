package com.dicoding.armand.storyapp.api

import com.dicoding.armand.storyapp.utils.ErrorUtils
import com.dicoding.armand.storyapp.utils.NetworkResult
import retrofit2.Response

@Suppress("BlockingMethodInNonBlockingContext")
abstract class ApiConfig {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let {
                    return NetworkResult.Success(it)
                }
            }
            ErrorUtils.error(ErrorUtils.getErrorMessage(response.errorBody()?.string()))
        } catch (e: Exception) {
            ErrorUtils.error("${e.message} : $e")
        }
    }
}