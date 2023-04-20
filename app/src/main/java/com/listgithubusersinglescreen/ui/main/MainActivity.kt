package com.listgithubusersinglescreen.ui.main

import android.app.SearchManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.listgithubusersinglescreen.R
import com.listgithubusersinglescreen.databinding.ActivityMainBinding
import com.listgithubusersinglescreen.helper.ListTheme
import com.listgithubusersinglescreen.ui.home.HomeFragment
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
        // manage toolbar on action bar
        setSupportActionBar(binding.mToolbar)
        // manage all fragment on anything case
        navHostFragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            setTheme()
            mainViewModel.clearAndSetTitle(binding.mToolbar, destination)
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

    private fun onBack() {
        navHostFragment.findNavController().let {
            if (it.currentDestination?.id == R.id.homeFragment) {
                finish()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ -> // navController, destination, bundle
                binding.mToolbar.let {
                    when (destination.id) {
                        // there isn't configure menu for SplashFragment and SettingsFragment
                        R.id.homeFragment -> {
                            menuInflater.inflate(R.menu.main_menu, menu)
                            searchView(menu!!)
                        }
                    }
                }
            }
        return super.onCreateOptionsMenu(menu)
    }

    private fun searchView(menu: Menu) {
        val searchManager = getSystemService<SearchManager>()
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.apply {
            homeViewModel.searchText.observe(this@MainActivity) { text ->
                searchView.setQuery(text, false)
            }

            setSearchableInfo(searchManager?.getSearchableInfo(componentName))
            queryHint = resources.getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView.clearFocus()
                    homeViewModel.setSearchText(query!!)
                    (navHostFragment.childFragmentManager.fragments[0] as HomeFragment)
                        .observeSearchUser(homeViewModel)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            android.R.id.home -> {
                onBack()
                true
            }

            R.id.love -> {
                Toast.makeText(this@MainActivity, "love list", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.setting -> {
                navHostFragment.findNavController()
                    .navigate(R.id.action_homeFragment_to_settingsFragment)
                true
            }

            R.id.share -> {
                Toast.makeText(this@MainActivity, "share", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(menuItem)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _navHostFragment = null
    }
}