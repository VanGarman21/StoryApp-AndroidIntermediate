package com.dicoding.armand.storyapp.view.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.armand.storyapp.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val dataRepository: Repository) : ViewModel() {

    suspend fun login(email: String, password: String) = dataRepository.login(email, password)

}