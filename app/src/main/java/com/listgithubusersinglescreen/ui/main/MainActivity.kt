package com.listgithubusersinglescreen.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.listgithubusersinglescreen.R
import com.listgithubusersinglescreen.databinding.ActivityMainBinding
import com.listgithubusersinglescreen.helper.ListTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModel<MainViewModel>()
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var _navHostFragment: NavHostFragment? = null
    private val navHostFragment get() = _navHostFragment!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        _navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        setupActionBarWithNavController(navHostFragment.navController)
        handleAllFragmentBehaviour()
        observeTheme()
    }

    private fun handleAllFragmentBehaviour() {
        // manage all fragment on anything case
        navHostFragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            configureUpButton(destination)
            configureActionBar(destination)
        }
    }

    private fun configureUpButton(destination: NavDestination) {
        if (destination.id != R.id.homeFragment) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

    fun onBack() {
        navHostFragment.findNavController().popBackStack()
    }

    private fun configureActionBar(destination: NavDestination) {
        if (destination.id == R.id.splashFragment) {
            supportActionBar?.hide()
        } else {
            supportActionBar?.show()
        }
    }

    private fun observeTheme() {
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            when (isDarkModeActive) {
                is ListTheme.DAY -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                is ListTheme.NIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                is ListTheme.UNKNOWN -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _navHostFragment = null
    }
}