package com.dicoding.armand.storyapp.utils

import com.dicoding.armand.storyapp.data.entity.Story
import com.dicoding.armand.storyapp.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {

    fun generateDummyStoriesResponse(): StoryResponse {
        val listStory = List(10) {
            ListStory(
                id = "story-FvU4u0Vp2S3PMsFg",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-01-08T06:34:18.598Z",
                name = "Dimas",
                description = "Lorem Ipsum",
                longitude = -16.002,
                latitude = -10.212
            )
        }
        return StoryResponse(error = false, message = "Stories fetched successfully", listStory = listStory)
    }

    fun generateDummyListStory(): List<Story> {
        return List(10) {
            Story(
                id = "story-FvU4u0Vp2S3PMsFg",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-01-08T06:34:18.598Z",
                name = "Dimas",
                description = "Lorem Ipsum",
                lon = -16.002,
                lat = -10.212
            )
        }
    }

    fun generateDummyLoginResponse(): ResponseLogin {
        val loginResult = LoginResult(
            userId = "story-DyGewy241D6ZmJI9",
            name = "new",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXNHamQzZWx0Wk1zckl1M3IiLCJpYXQiOjE2NTcyMTc2NjV9.ZlZaTNeZX3Db4KYwTkIaiUTBy5oX-3DkSmlSnpSuZws"
        )
        return ResponseLogin(loginResult = loginResult, error = false, message = "success")
    }

    fun generateDummyRegisterResponse(): ResponseSignUp {
        return ResponseSignUp(error = false, message = "success")
    }

    fun generateDummyMultipartFile(): MultipartBody.Part {
        val dummyText = "text"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun generateDummyRequestBody(): String {
        return "text"
    }

    fun generateDummyFileUploadResponse(): ResponseAddStory {
        return ResponseAddStory(error = false, message = "success")
    }

    fun generateDummyToken(): String {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXNHamQzZWx0Wk1zckl1M3IiLCJpYXQiOjE2NTcyMTc2NjV9.ZlZaTNeZX3Db4KYwTkIaiUTBy5oX-3DkSmlSnpSuZws"
    }
}