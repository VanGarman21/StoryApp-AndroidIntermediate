package com.dicoding.armand.storyapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.armand.storyapp.R
import com.dicoding.armand.storyapp.utils.UserPreference

@SuppressLint("CustomSplashScreen")
class WelcomeActivity<Button : View?> : AppCompatActivity() {

    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        initialize()
    }

    private fun initialize() {
        userPreference = UserPreference(this)
        setupView()
        val buttonStart = findViewById<Button>(R.id.button_start)
            if(userPreference.isEnabled) {
                Intent(this@WelcomeActivity, MainActivity::class.java)
                buttonStart?.setOnClickListener {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            } else {
                Intent(this@WelcomeActivity, WelcomeActivity::class.java)
                buttonStart?.setOnClickListener {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}