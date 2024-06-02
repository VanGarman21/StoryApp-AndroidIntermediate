package com.dicoding.armand.storyapp.view.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.armand.storyapp.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val dataRepository: Repository) : ViewModel() {

    suspend fun register(name: String, email: String, password: String) = dataRepository.register(name, email, password)

}