package com.dicoding.armand.storyapp.data

import com.dicoding.armand.storyapp.utils.CoroutinesTestRule
import com.dicoding.armand.storyapp.utils.DataDummy
import com.dicoding.armand.storyapp.utils.NetworkResult
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RepositoryTest {
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var source: Source
    private lateinit var repository: Repository

    private val dummyToken = DataDummy.generateDummyToken()
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyRequestBody()
    private val dummyName = "new"
    private val dummyEmail = "new23@gmail.com"
    private val dummyPassword = "12345678"

    @Before
    fun setup() {
        repository = Repository(source)
    }

    @Test
    fun `Login successfully`() = runTest {
        val expectedResponse = DataDummy.generateDummyLoginResponse()
        `when`(source.login(dummyEmail, dummyPassword)).thenReturn(Response.success(expectedResponse))

        repository.login(dummyEmail, dummyPassword).collect { result ->
            when (result) {
                is NetworkResult.Success -> {
                    assertTrue(true)
                    assertNotNull(result.resultData)
                    assertEquals(expectedResponse, result.resultData)
                }
                is NetworkResult.Error -> {
                    assertFalse(result.resultData!!.error)
                    assertNull(result)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    @Test
    fun `Login Failed`() = runTest {
        `when`(source.login(dummyEmail, dummyPassword)).then { throw Exception() }

        repository.login(dummyEmail, dummyPassword).collect { result ->
            when (result) {
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
    }

    @Test
    fun `Register successfully`() = runTest {
        val expectedResponse = DataDummy.generateDummyRegisterResponse()
        `when`(source.register(dummyName, dummyEmail, dummyPassword)).thenReturn(Response.success(expectedResponse))

        repository.register(dummyName, dummyEmail, dummyPassword).collect { result ->
            when (result) {
                is NetworkResult.Success -> {
                    assertTrue(true)
                    assertNotNull(result.resultData)
                    assertEquals(expectedResponse, result.resultData)
                }
                is NetworkResult.Error -> {
                    assertFalse(result.resultData!!.error)
                    assertNull(result)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    @Test
    fun `User register failed`() = runTest {
        `when`(source.register(dummyName, dummyEmail, dummyPassword)).then { throw Exception() }

        repository.register(dummyName, dummyEmail, dummyPassword).collect { result ->
            when (result) {
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
    }

    @Test
    fun `Upload image file - successfully`() = runTest {
        val expectedResponse = DataDummy.generateDummyFileUploadResponse()
        `when`(source.uploadStory("Bearer $dummyToken", "text", "", "", dummyMultipart)).thenReturn(Response.success(expectedResponse))

        repository.uploadStory(dummyToken, "text", "", "", dummyMultipart).collect { result ->
            when (result) {
                is NetworkResult.Success -> {
                    assertNotNull(result.resultData)
                    assertTrue(true)
                }
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
            }
        }

        verify(source).uploadStory("Bearer $dummyToken", "text", "", "", dummyMultipart)
    }

    @Test
    fun `Upload image file - throw exception`() = runTest {
        `when`(source.uploadStory("Bearer $dummyToken", dummyDescription, "", "", dummyMultipart)).then { throw Exception() }

        repository.uploadStory(dummyToken, dummyDescription, "", "", dummyMultipart).collect { result ->
            when (result) {
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

        verify(source).uploadStory("Bearer $dummyToken", dummyDescription, "", "", dummyMultipart)
    }
}