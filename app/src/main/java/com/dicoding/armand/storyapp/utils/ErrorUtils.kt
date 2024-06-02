package com.dicoding.armand.storyapp.utils

import org.json.JSONObject

object ErrorUtils {
    fun getErrorMessage(message: String?): String {
        return message?.let {
            val obj = JSONObject(it)
            obj.getString("message")
        } ?: "Unknown error"
    }

    fun <T> error(errorMessage: String, data: T? = null): NetworkResult<T> =
        NetworkResult.Error(errorMessage, data)
}