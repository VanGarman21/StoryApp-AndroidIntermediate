package com.dicoding.armand.storyapp.viewmodel

import com.dicoding.armand.storyapp.data.Repository
import com.dicoding.armand.storyapp.data.response.ResponseSignUp
import com.dicoding.armand.storyapp.utils.CoroutinesTestRule
import com.dicoding.armand.storyapp.utils.DataDummy
import com.dicoding.armand.storyapp.utils.NetworkResult
import com.dicoding.armand.storyapp.view.viewmodel.RegisterViewModel
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var RegisterViewModel: RegisterViewModel

    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private val dummyName = "new"
    private val dummyEmail = "new23@gmail.com"
    private val dummyPassword = "12345678"

    @Before
    fun setup() {
        RegisterViewModel = RegisterViewModel(repository)
    }

    @Test
    fun `Register is successfully - NetworkResult Success`(): Unit = runTest {
        val expectedResponse = flowOf(NetworkResult.Success(dummyRegisterResponse))

        `when`(RegisterViewModel.register(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        RegisterViewModel.register(dummyName, dummyEmail, dummyPassword).collect { result ->
            when(result) {
                is NetworkResult.Success -> {
                    assertTrue(true)
                    assertNotNull(result.resultData)
                    assertSame(result.resultData, dummyRegisterResponse)
                }
                is NetworkResult.Error -> {
                    assertFalse(result.resultData!!.error)
                }
                is NetworkResult.Loading ->{}
            }
        }
        verify(repository).register(dummyName, dummyEmail, dummyPassword)
    }

    @Test
    fun `Register is Failed - NetworkResult Failed`(): Unit = runTest {
        val expectedResponse : Flow<NetworkResult<ResponseSignUp>> = flowOf(NetworkResult.Error("failed"))

        `when`(RegisterViewModel.register(dummyName,dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        RegisterViewModel.register(dummyName, dummyEmail, dummyPassword).collect { result ->
            when(result) {
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
        verify(repository).register(dummyName,dummyEmail, dummyPassword)
    }
}