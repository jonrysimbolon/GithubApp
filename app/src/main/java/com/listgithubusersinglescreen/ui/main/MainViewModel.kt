package com.listgithubusersinglescreen.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.listgithubusersinglescreen.helper.ListTheme
import com.listgithubusersinglescreen.repository.settings.PreferencesParent

class MainViewModel(
    private val pref: PreferencesParent
): ViewModel() {
    fun getThemeSettings(): LiveData<ListTheme> = pref.getThemeSetting().asLiveData()
}