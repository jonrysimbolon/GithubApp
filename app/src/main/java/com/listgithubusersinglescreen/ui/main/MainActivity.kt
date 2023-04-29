package com.listgithubusersinglescreen.ui.main

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.listgithubusersinglescreen.R
import com.listgithubusersinglescreen.databinding.ActivityMainBinding
import com.listgithubusersinglescreen.helper.ListTheme
import com.listgithubusersinglescreen.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModel<MainViewModel>()
    private val homeViewModel by viewModel<HomeViewModel>()
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
        handleAllFragmentBehaviour()
    }

    private fun handleAllFragmentBehaviour() {
        // manage all fragment on anything case
        navHostFragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            title = destination.label
            setTheme()
            configureActionBar(destination)
            configureUpButton(destination)
        }
        // manage back button
        backPressed()
    }

    private fun backPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        })
    }

    fun onBack() {
        navHostFragment.findNavController().let {
            if (it.currentDestination?.id == R.id.homeFragment) {
                if(homeViewModel.isSearch.value == true){
                    println("is search main nya true")
                    homeViewModel.isSearch.value = false
                    homeViewModel.getFreshUser()
                }else{
                    println("is search main nya false")
                    finish()
                }
            } else {
                it.popBackStack()
            }
        }
    }

    private fun configureUpButton(destination: NavDestination) {
        if (destination.id != R.id.homeFragment) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun configureActionBar(destination: NavDestination) {
        if (destination.id == R.id.splashFragment) {
            supportActionBar?.hide()
        } else {
            supportActionBar?.show()
        }
    }

    private fun setTheme() {
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