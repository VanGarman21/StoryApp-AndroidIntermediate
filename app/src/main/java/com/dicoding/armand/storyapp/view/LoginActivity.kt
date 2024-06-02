package com.dicoding.armand.storyapp.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.dicoding.armand.storyapp.R
import com.dicoding.armand.storyapp.data.response.ResponseLogin
import com.dicoding.armand.storyapp.databinding.ActivityLoginBinding
import com.dicoding.armand.storyapp.utils.Message
import com.dicoding.armand.storyapp.utils.NetworkResult
import com.dicoding.armand.storyapp.utils.UserPreference
import com.dicoding.armand.storyapp.view.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var userPreference: UserPreference
    private var loginJob: Job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        userPreference = UserPreference(this)
        userPreference.isExampleLogin = true
        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        playAnimations()
    }

    private fun setupListeners() {
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
        setLogin()
    }

    private fun playAnimations() {
        ObjectAnimator.ofFloat(binding.imgLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val animations = listOf(
            ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500),
            ObjectAnimator.ofFloat(binding.textView2, View.ALPHA, 1f).setDuration(500),
            ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500),
            ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500),
            ObjectAnimator.ofFloat(binding.buttonSignIn, View.ALPHA, 1f).setDuration(500)
        )

        AnimatorSet().apply {
            playSequentially(animations)
            start()
        }
    }

    private fun setLogin() {
        binding.buttonSignIn.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Message.showToastMessage(this, getString(R.string.warning_input))
            } else {
                performLogin(email, password)
            }
        }
    }

    private fun performLogin(email: String, password: String) {
        setLoadingState(true)
        lifecycle.coroutineScope.launchWhenResumed {
            if (loginJob.isActive) loginJob.cancel()
            loginJob = launch {
                viewModel.login(email, password).collect { result ->
                    handleLoginResult(result)
                }
            }
        }
    }

    private fun handleLoginResult(result: NetworkResult<ResponseLogin>) {
        when (result) {
            is NetworkResult.Success -> {
                userPreference.isEnabled = !result.resultData?.error!!
                userPreference.authToken = result.resultData.loginResult.token
                navigateToMain()
                setLoadingState(false)
            }
            is NetworkResult.Loading -> setLoadingState(true)
            is NetworkResult.Error -> {
                Message.showToastMessage(this, getString(R.string.check))
                setLoadingState(false)
            }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setLoadingState(loading: Boolean) {
        binding.buttonSignIn.visibility = if (loading) View.INVISIBLE else View.VISIBLE
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }
}