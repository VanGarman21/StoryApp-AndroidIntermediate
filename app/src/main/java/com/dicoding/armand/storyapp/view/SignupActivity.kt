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
import com.dicoding.armand.storyapp.databinding.ActivitySignupBinding
import com.dicoding.armand.storyapp.utils.Message
import com.dicoding.armand.storyapp.utils.NetworkResult
import com.dicoding.armand.storyapp.view.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {
    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    private val viewModel: RegisterViewModel by viewModels()
    private var registerJob: Job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupActionBar()
        setupListeners()
        playPropertyAnimation()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = resources.getString(R.string.register)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupListeners() {
        binding.tvSignIn.setOnClickListener {
            navigateToLogin()
        }
        binding.buttonRegister.setOnClickListener {
            handleRegister()
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun playPropertyAnimation() {
        ObjectAnimator.ofFloat(binding.imgRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val animations = listOf(
            ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500),
            ObjectAnimator.ofFloat(binding.edtUsername, View.ALPHA, 1f).setDuration(500),
            ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500),
            ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500),
            ObjectAnimator.ofFloat(binding.buttonRegister, View.ALPHA, 1f).setDuration(500)
        )

        AnimatorSet().apply {
            playSequentially(animations)
            start()
        }
    }

    private fun handleRegister() {
        val name = binding.edtUsername.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name)) {
            Message.showToastMessage(this, getString(R.string.warning_input))
        } else {
            setLoadingState(true)
            lifecycle.coroutineScope.launchWhenResumed {
                if (registerJob.isActive) registerJob.cancel()
                registerJob = launch {
                    viewModel.register(name, email, password).collect { result ->
                        handleRegisterResult(result)
                    }
                }
            }
        }
    }

    private fun handleRegisterResult(result: NetworkResult<*>) {
        when (result) {
            is NetworkResult.Success -> {
                setLoadingState(false)
                navigateToLogin()
                Message.showToastMessage(this, getString(R.string.success_register))
            }
            is NetworkResult.Loading -> setLoadingState(true)
            is NetworkResult.Error -> {
                Message.showToastMessage(this, getString(R.string.error_register))
                setLoadingState(false)
            }
            else -> {}
        }
    }

    private fun setLoadingState(loading: Boolean) {
        binding.buttonRegister.visibility = if (loading) View.INVISIBLE else View.VISIBLE
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}