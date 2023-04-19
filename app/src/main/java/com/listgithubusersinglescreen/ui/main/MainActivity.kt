package com.listgithubusersinglescreen.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import com.listgithubusersinglescreen.R
import com.listgithubusersinglescreen.databinding.ActivityMainBinding
import com.listgithubusersinglescreen.helper.ListTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupTheme()
        setupToolbar()
        backPressed()
    }

    private fun backPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                binding.container.findNavController().let {
                    if(it.currentDestination?.id == R.id.homeFragment){
                        finish()
                    }else{
                        it.popBackStack()
                    }
                }
            }
        })
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.mToolbar)
    }

    private fun setupTheme() {
        // get default system theme
        val systemDefaultTheme =
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_NO
            }

        viewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            when (isDarkModeActive) {
                is ListTheme.DAY -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                is ListTheme.NIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                is ListTheme.UNKNOWN -> AppCompatDelegate.setDefaultNightMode(systemDefaultTheme)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}