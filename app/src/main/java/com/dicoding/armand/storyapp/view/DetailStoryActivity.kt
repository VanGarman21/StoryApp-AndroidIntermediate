package com.dicoding.armand.storyapp.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.armand.storyapp.R
import com.dicoding.armand.storyapp.data.entity.Story
import com.dicoding.armand.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private val binding: ActivityDetailStoryBinding by lazy {
        ActivityDetailStoryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupActionBar()
        fetchData()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = getString(R.string.details)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun fetchData() {
        intent.getParcelableExtra<Story>(EXTRA_ITEM)?.let { storyItem ->
            binding.apply {
                tvName.text = storyItem.name
                tvDescription.text = storyItem.description
                Glide.with(this@DetailStoryActivity)
                    .load(storyItem.photoUrl)
                    .into(imgPhotos)
            }
        } ?: run {
            showError()
        }
    }

    private fun showError() {
        Toast.makeText(this, "Failed to retrieve story details", Toast.LENGTH_SHORT).show()
        finish()
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_ITEM = "extra_item"
    }
}