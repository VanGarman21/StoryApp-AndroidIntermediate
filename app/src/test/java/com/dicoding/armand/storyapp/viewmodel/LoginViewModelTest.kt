package com.dicoding.armand.storyapp.viewmodel

import com.dicoding.armand.storyapp.data.Repository
import com.dicoding.armand.storyapp.data.response.ResponseLogin
import com.dicoding.armand.storyapp.utils.CoroutinesTestRule
import com.dicoding.armand.storyapp.utils.DataDummy
import com.dicoding.armand.storyapp.utils.NetworkResult
import com.dicoding.armand.storyapp.view.viewmodel.LoginViewModel
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
class LoginViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var loginViewModel: LoginViewModel

    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyEmail = "new23@gmail.com"
    private val dummyPassword = "12345678"

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(repository)
    }

    @Test
    fun `Login is successfully - NetworkResult Success`(): Unit = runTest {
        val expectedResponse = flowOf(NetworkResult.Success(dummyLoginResponse))

        `when`(loginViewModel.login(dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        loginViewModel.login(dummyEmail, dummyPassword).collect { result ->
            when(result) {
                is NetworkResult.Success -> {
                    assertTrue(true)
                    assertNotNull(result.resultData)
                    assertSame(result.resultData, dummyLoginResponse)
                }
                is NetworkResult.Error -> {
                    assertFalse(result.resultData!!.error)
                }
                is NetworkResult.Loading ->{}
            }
        }
        verify(repository).login(dummyEmail, dummyPassword)
    }

    @Test
    fun `Login is Failed - NetworkResult Failed`(): Unit = runTest {
        val expectedResponse : Flow<NetworkResult<ResponseLogin>> = flowOf(NetworkResult.Error("failed"))

        `when`(loginViewModel.login(dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        loginViewModel.login(dummyEmail, dummyPassword).collect { result ->
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
        verify(repository).login(dummyEmail, dummyPassword)
    }
}