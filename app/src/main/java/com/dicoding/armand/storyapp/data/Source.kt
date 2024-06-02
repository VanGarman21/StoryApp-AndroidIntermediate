package com.dicoding.armand.storyapp.data

import com.dicoding.armand.storyapp.data.response.ResponseAddStory
import com.dicoding.armand.storyapp.api.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

class Source @Inject constructor(private val apiService: ApiService) {

    suspend fun login(email: String, password: String) = 
        apiService.login(email, password)

    suspend fun register(name: String, email: String, password: String) = 
        apiService.register(name, email, password)

    suspend fun uploadStory(auth: String, description: String, lat: String?, lon: String?, file: MultipartBody.Part): Response<ResponseAddStory> {
        val desc = description.toRequestBody("text/plain".toMediaType())
        val latitude = lat?.toRequestBody("text/plain".toMediaType())
        val longitude = lon?.toRequestBody("text/plain".toMediaType())
        return apiService.uploadStory(auth, file, desc, latitude, longitude)
    }

    suspend fun getStoriesLocation(auth: String) = 
        apiService.getStories(auth = auth, location = 1)
}