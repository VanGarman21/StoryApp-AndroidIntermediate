package com.dicoding.armand.storyapp.data

import com.dicoding.armand.storyapp.api.ApiConfig
import com.dicoding.armand.storyapp.data.response.ResponseAddStory
import com.dicoding.armand.storyapp.data.response.ResponseLogin
import com.dicoding.armand.storyapp.data.response.ResponseSignUp
import com.dicoding.armand.storyapp.data.response.StoryResponse
import com.dicoding.armand.storyapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val source: Source
) : ApiConfig() {

    suspend fun getStoriesLocation(auth: String): Flow<NetworkResult<StoryResponse>> =
        executeApiCall {
            source.getStoriesLocation(generateAuthorization(auth))
        }

    suspend fun uploadStory(
        auth: String,
        description: String,
        lat: String?,
        lon: String?,
        file: MultipartBody.Part
    ): Flow<NetworkResult<ResponseAddStory>> =
        executeApiCall {
            val generateToken = generateAuthorization(auth)
            source.uploadStory(generateToken, description, lat, lon, file)
        }

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<NetworkResult<ResponseSignUp>> =
        executeApiCall {
            source.register(name, email, password)
        }

    suspend fun login(email: String, password: String): Flow<NetworkResult<ResponseLogin>> =
        executeApiCall {
            source.login(email, password)
        }

    private fun generateAuthorization(token: String): String {
        return "Bearer $token"
    }

    private fun <T> executeApiCall(apiCall: suspend () -> Response<T>): Flow<NetworkResult<T>> =
        flow {
            emit(safeApiCall { apiCall() })
        }.flowOn(Dispatchers.IO)
}