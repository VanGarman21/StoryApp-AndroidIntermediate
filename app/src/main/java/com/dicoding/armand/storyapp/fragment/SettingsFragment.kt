package com.dicoding.armand.storyapp.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dicoding.armand.storyapp.R
import com.dicoding.armand.storyapp.databinding.FragmentSettingsBinding
import com.dicoding.armand.storyapp.view.LoginActivity
import com.dicoding.armand.storyapp.utils.Message
import com.dicoding.armand.storyapp.utils.UserPreference

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        userPreference = UserPreference(requireContext())
        setupListeners()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.title = resources.getString(R.string.settings)
        }
        binding.toolbar.apply {
            setTitleTextColor(Color.WHITE)
            setSubtitleTextColor(Color.WHITE)
        }
    }

    private fun setupListeners() {
        binding.logOut.setOnClickListener { showLogoutDialog() }
        binding.settingsLanguage.setOnClickListener { openLanguageSettings() }
    }

    private fun showLogoutDialog() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle(resources.getString(R.string.log_out))
        dialog.setMessage(getString(R.string.are_you_sure))
        dialog.setPositiveButton(getString(R.string.yes)) { _, _ ->
            userPreference.clearPreferences()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            activity?.finish()
            Message.showToastMessage(requireContext(), getString(R.string.log_out_warning))
        }
        dialog.setNegativeButton(getString(R.string.no)) { _, _ ->
            Message.showToastMessage(requireContext(), getString(R.string.not_out))
        }
        dialog.show()
    }

    private fun openLanguageSettings() {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}