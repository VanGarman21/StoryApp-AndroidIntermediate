package com.dicoding.armand.storyapp.viewmodel

import androidx.paging.ExperimentalPagingApi
import com.dicoding.armand.storyapp.data.Repository
import com.dicoding.armand.storyapp.data.response.ResponseAddStory
import com.dicoding.armand.storyapp.utils.DataDummy
import com.dicoding.armand.storyapp.utils.NetworkResult
import com.dicoding.armand.storyapp.view.viewmodel.UploadStoryViewModel
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UploadStoryViewModelTest {

    @Mock
    private lateinit var repository: Repository

    private lateinit var uploadStoryViewModel: UploadStoryViewModel

    private val dummyToken = DataDummy.generateDummyToken()
    private val dummyUploadResponse = DataDummy.generateDummyFileUploadResponse()
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyRequestBody()

    @Before
    fun setUp() {
        uploadStoryViewModel = UploadStoryViewModel(repository)
    }

    @Test
    fun `Upload file successfully`() = runTest {
        val expectedResponse = flowOf(NetworkResult.Success(dummyUploadResponse))

        Mockito.`when`(
            repository.uploadStory(
                dummyToken,
                dummyDescription,
                "",
                "",
                dummyMultipart
            )
        ).thenReturn(expectedResponse)

        uploadStoryViewModel.uploadStory(dummyToken, dummyDescription, "", "", dummyMultipart)
            .collect { result ->
                when(result){
                    is NetworkResult.Success -> {
                        assertNotNull(result.resultData)
                        assertTrue(true)
                        assertSame(dummyUploadResponse, result.resultData)
                    }
                    is NetworkResult.Error -> assertFalse(result.resultData!!.error)
                    is NetworkResult.Loading ->{}
                }
            }

        verify(repository).uploadStory(dummyToken, dummyDescription, "", "", dummyMultipart)
    }

    @Test
    fun `Upload file Failed`() = runTest {
        val expectedResponse : Flow<NetworkResult<ResponseAddStory>> = flowOf(NetworkResult.Error("failed"))

        Mockito.`when`(
            repository.uploadStory(
                dummyToken,
                dummyDescription,
                "",
                "",
                dummyMultipart
            )
        ).thenReturn(expectedResponse)

        uploadStoryViewModel.uploadStory(dummyToken, dummyDescription, "", "", dummyMultipart)
            .collect { result ->
                when(result){
                    is NetworkResult.Success -> {
                        assertTrue(false)
                        assertFalse(result.resultData!!.error)
                    }
                    is NetworkResult.Error -> {
                        assertNotNull(result.resultMessage)
                    }
                    is NetworkResult.Loading ->{}
                }
            }

        verify(repository).uploadStory(dummyToken, dummyDescription, "", "", dummyMultipart)
    }
}