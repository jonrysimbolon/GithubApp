package com.listgithubusersinglescreen.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
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

    private val viewModel by viewModel<MainViewModel>()
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var _navHostFragment: NavHostFragment? = null
    private val navHostFragment get() = _navHostFragment!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        _navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        setTheme()
        setToolbar()
        backPressed()
    }

    private fun backPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                onBack()
            }
        })
    }

    private fun onBack() {
        navHostFragment.findNavController().let {
            if(it.currentDestination?.id == R.id.homeFragment){
                finish()
            }else{
                it.popBackStack()
            }
        }
    }

    private fun setToolbar() {
        setSupportActionBar(binding.mToolbar)
        binding.mToolbar.let {
            navHostFragment.findNavController().addOnDestinationChangedListener{ _, destination, _ ->

                it.menu.clear()
                it.title = destination.label

                if(destination.id == R.id.splashFragment){
                    supportActionBar?.hide()
                }else{
                    supportActionBar?.show()
                }

                if(destination.id != R.id.homeFragment){
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }else{
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }

                when(destination.id){
                    R.id.splashFragment -> {

                    }
                    R.id.homeFragment -> {
                        it.inflateMenu(R.menu.main_menu)
                    }
                    R.id.settingsFragment -> {

                    }
                }
            }
        }
    }

    private fun setTheme() {
        viewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            println("theme : $isDarkModeActive")
            when (isDarkModeActive) {
                is ListTheme.DAY -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                is ListTheme.NIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                is ListTheme.UNKNOWN -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }


    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> {
                onBack()
                return true
            }
            R.id.love -> {
                Toast.makeText(this@MainActivity, "love list", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.setting -> {
                Toast.makeText(this@MainActivity, "setting list", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.share -> {
                Toast.makeText(this@MainActivity, "share", Toast.LENGTH_SHORT).show()
                /*startActivity(
                    Intent.createChooser(
                        Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, urlString)
                            type = getString(R.string.type_share)
                        }, getString(R.string.share_to)
                    )
                )*/
                return true
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _navHostFragment = null
    }
}