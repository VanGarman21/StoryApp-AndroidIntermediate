package com.dicoding.armand.storyapp.fragment

import android.os.Bundle
import androidx.paging.ExperimentalPagingApi
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.dicoding.armand.storyapp.R
import com.dicoding.armand.storyapp.data.response.StoryResponse
import com.dicoding.armand.storyapp.di.ApiModule
import com.dicoding.armand.storyapp.utils.EspressoIdlingResource
import com.dicoding.armand.storyapp.utils.JsonConverter
import com.dicoding.armand.storyapp.utils.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
@ExperimentalPagingApi
@HiltAndroidTest
class StoryFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        mockWebServer.start(8080)
        ApiModule.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun launchStoryFragmentSuccess() {
        launchFragmentInHiltContainer<StoryFragment>(Bundle(), R.style.Theme_StoryApp)
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.rv_story)).check(matches(isDisplayed()))

        // Memastikan data tidak null dan jumlah data sesuai
        val stories: StoryResponse = JsonConverter.fromJsonFile("success_response.json", StoryResponse::class.java)
        assertNotNull(stories)
        assertEquals(1, stories.listStory.size)

        // Memastikan data pertama yang dikembalikan sesuai
        val firstStory = stories.listStory[0]
        assertEquals("Dimas", firstStory.name)
    }

    @Test
    fun launchStoryFragmentEmptyOrError() {
        launchFragmentInHiltContainer<StoryFragment>(Bundle(), R.style.Theme_StoryApp)
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response_empty.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.tv_not_found))
            .check(matches(not(isDisplayed())))

        // Memastikan jumlah data yang dikembalikan nol
        val stories: StoryResponse = JsonConverter.fromJsonFile("success_response_empty.json", StoryResponse::class.java)
        assertNotNull(stories)
        assertEquals(0, stories.listStory.size)
    }

}