package com.dicoding.armand.storyapp.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.armand.storyapp.R
import com.dicoding.armand.storyapp.databinding.CardStoryBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.armand.storyapp.data.entity.Story
import com.dicoding.armand.storyapp.utils.DateFormatter
import java.util.*

class StoryAdapter : PagingDataAdapter<Story, StoryAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: CardStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(context: Context, story: Story) {
            setData(story)
            setImage(context, story.photoUrl)
            setDate(story.createdAt)
            setOnClickListener(context, story)
        }

        private fun setData(story: Story) {
            binding.apply {
                tvName.text = story.name
                tvDescription.text = story.description
            }
        }

        private fun setImage(context: Context, photoUrl: String) {
            val options = RequestOptions()
                .placeholder(R.drawable.baseline_refresh_24)
                .error(R.drawable.baseline_error_outline_24)
            Glide.with(context).load(photoUrl).apply(options).into(binding.imgPhotos)
        }

        private fun setDate(createdAt: String) {
            binding.tvDate.text = DateFormatter.formatDate(createdAt, TimeZone.getDefault().id)
        }

        private fun setOnClickListener(context: Context, story: Story) {
            binding.root.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        binding.root.context as Activity,
                        Pair(binding.imgPhotos, "image"),
                        Pair(binding.tvName, "name"),
                        Pair(binding.tvDescription, "description")
                    )

                Intent(context, DetailStoryActivity::class.java).also { intent ->
                    intent.putExtra(DetailStoryActivity.EXTRA_ITEM, story)
                    context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            Log.d("StoryAdapter", "Binding data: ${story.name}")
            holder.bind(holder.itemView.context, story)
        }
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }
}

