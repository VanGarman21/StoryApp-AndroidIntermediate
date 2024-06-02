package com.dicoding.armand.storyapp.view.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.armand.storyapp.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class UploadStoryViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    suspend fun uploadStory(authToken: String, description: String, lat: String?, lon: String?, storyFile: MultipartBody.Part) =
        repository.uploadStory(authToken, description, lat, lon, storyFile)


}