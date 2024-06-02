package com.dicoding.armand.storyapp.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.armand.storyapp.R
import com.dicoding.armand.storyapp.databinding.FragmentStoryBinding
import com.dicoding.armand.storyapp.utils.EspressoIdlingResource
import com.dicoding.armand.storyapp.utils.UserPreference
import com.dicoding.armand.storyapp.view.AddStoryActivity
import com.dicoding.armand.storyapp.view.LoadingStateAdapter
import com.dicoding.armand.storyapp.view.StoryAdapter
import com.dicoding.armand.storyapp.view.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalPagingApi
class StoryFragment : Fragment() {
    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var userPreference: UserPreference
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoryBinding.inflate(LayoutInflater.from(requireActivity()))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreference = UserPreference(requireContext())
        storyAdapter = StoryAdapter()
        setupToolbar()
        setupMenu()
        setPagingData()
        getData()
        setupListeners()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding?.toolbar)
            supportActionBar?.title = resources.getString(R.string.app_name)
        }
        binding?.toolbar?.apply {
            setTitleTextColor(Color.WHITE)
            setSubtitleTextColor(Color.WHITE)
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Inflate your menu here
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupListeners() {
        binding?.fabAddStory?.setOnClickListener {
            startActivity(Intent(requireContext(), AddStoryActivity::class.java))
        }
    }

    private fun getData() {
        EspressoIdlingResource.increment() // Tambahkan ini
        viewModel.fetchStories(userPreference.authToken).observe(viewLifecycleOwner) {
            Log.d("StoryFragment", "Data received: ${it}")
            storyAdapter.submitData(lifecycle, it)
            EspressoIdlingResource.decrement() // Tambahkan ini
        }
    }

    private fun setPagingData() {
        binding?.apply {
            rvStory.setHasFixedSize(true)
            rvStory.layoutManager = LinearLayoutManager(requireContext())
            rvStory.adapter = storyAdapter.withLoadStateHeaderAndFooter(
                header = LoadingStateAdapter { storyAdapter.retry() },
                footer = LoadingStateAdapter { storyAdapter.retry() }
            )
            swipeRefresh.setOnRefreshListener {
                storyAdapter.refresh()
                swipeRefresh.isRefreshing = false
                rvStory.visibility = View.GONE
            }
            btnTry.setOnClickListener {
                storyAdapter.retry()
            }
        }

        storyAdapter.addLoadStateListener {
            binding?.apply {
                progressBar.isVisible = it.source.refresh is LoadState.Loading
                rvStory.isVisible = it.source.refresh is LoadState.NotLoading
                tvError.isVisible = it.source.refresh is LoadState.Error || (it.append.endOfPaginationReached && storyAdapter.itemCount < 1)
                btnTry.isVisible = it.source.refresh is LoadState.Error

                if (it.source.refresh is LoadState.NotLoading &&
                    it.append.endOfPaginationReached &&
                    storyAdapter.itemCount < 1
                ) {
                    tvNotFound.isVisible = true
                    rvStory.isVisible = false
                } else {
                    tvNotFound.isVisible = false
                    rvStory.isVisible = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
