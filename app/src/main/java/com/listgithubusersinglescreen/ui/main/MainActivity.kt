package com.listgithubusersinglescreen.ui.main

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
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
        // manage back button
        configBackPressed()
        // manage theme
        configTheme()
        // manage action bar
        configActionBar()
    }

    private fun configActionBar() {
        navHostFragment.findNavController().addOnDestinationChangedListener{ _, destination, _ ->
            if(destination.id == R.id.splashFragment){
                supportActionBar?.hide()
            }else{
                supportActionBar?.show()
            }
        }
    }

    private fun configBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        })
    }

    private fun onBack() {
        navHostFragment.findNavController().let {
            if (it.currentDestination?.id == R.id.homeFragment) {
                finish()
            } else {
                it.popBackStack()
            }
        }
    }

    private fun configTheme() {
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