package com.dicoding.armand.storyapp.viewmodel

import androidx.paging.ExperimentalPagingApi
import com.dicoding.armand.storyapp.data.Repository
import com.dicoding.armand.storyapp.data.response.StoryResponse
import com.dicoding.armand.storyapp.utils.DataDummy
import com.dicoding.armand.storyapp.utils.NetworkResult
import com.dicoding.armand.storyapp.view.viewmodel.MapsViewModel
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @Mock
    private lateinit var repository: Repository
    private lateinit var mapsViewModel: MapsViewModel

    private val dummyStoriesResponse = DataDummy.generateDummyStoriesResponse()
    private val dummyToken = DataDummy.generateDummyToken()

    @Before
    fun setup() {
        mapsViewModel = MapsViewModel(repository)
    }

    @Test
    fun `Get location story is successfully - NetworkResult Success`(): Unit = runTest {
        val expectedResponse = flowOf(NetworkResult.Success(dummyStoriesResponse))

        `when`(repository.getStoriesLocation(dummyToken)).thenReturn(expectedResponse)

        mapsViewModel.getStoriesLocation(dummyToken).collect { result ->
            when(result) {
                is NetworkResult.Success -> {
                    assertTrue(true)
                    assertNotNull(result.resultData)
                    assertSame(result.resultData, dummyStoriesResponse)
                }
                is NetworkResult.Error -> {
                    assertFalse(result.resultData!!.error)
                }
                is NetworkResult.Loading -> {}
            }
        }

        verify(repository).getStoriesLocation(dummyToken)
    }

    @Test
    fun `Get location story is failed - NetworkResult Error`(): Unit = runTest {
        val expectedResponse: Flow<NetworkResult<StoryResponse>> = flowOf(NetworkResult.Error("failed"))

        `when`(repository.getStoriesLocation(dummyToken)).thenReturn(expectedResponse)

        mapsViewModel.getStoriesLocation(dummyToken).collect { result ->
            when(result) {
                is NetworkResult.Success -> {
                    assertTrue(false)
                    assertFalse(result.resultData!!.error)
                }
                is NetworkResult.Error -> {
                    assertNotNull(result.resultMessage)
                }
                is NetworkResult.Loading -> {}
            }
        }

        verify(repository).getStoriesLocation(dummyToken)
    }
}