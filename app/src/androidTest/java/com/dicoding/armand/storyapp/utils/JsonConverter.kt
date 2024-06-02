package com.dicoding.armand.storyapp.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import java.io.InputStreamReader

object JsonConverter {

    fun readStringFromFile(filename: String): String {
        try {
            val applicationContext = ApplicationProvider.getApplicationContext<Context>()
            val inputStream = applicationContext.assets.open(filename)
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }

            return builder.toString()
        } catch (e: Exception) {
            throw e
        }
    }

    fun <T> fromJsonFile(fileName: String, classOfT: Class<T>): T {
        val json = readStringFromFile(fileName)
        return Gson().fromJson(json, classOfT)
    }
}