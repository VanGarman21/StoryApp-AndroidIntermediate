package com.dicoding.armand.storyapp.data.response

import com.google.gson.annotations.SerializedName

data class ResponseSignUp(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,
)
