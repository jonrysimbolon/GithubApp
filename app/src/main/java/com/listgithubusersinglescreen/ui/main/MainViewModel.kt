package com.listgithubusersinglescreen.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.navigation.NavDestination
import com.google.android.material.appbar.MaterialToolbar
import com.listgithubusersinglescreen.helper.ListTheme
import com.listgithubusersinglescreen.repository.settings.PreferencesParent

class MainViewModel(
    private val pref: PreferencesParent
): ViewModel() {
    fun getThemeSettings(): LiveData<ListTheme> = pref.getThemeSetting().asLiveData()

    fun clearAndSetTitle(it: MaterialToolbar, destination: NavDestination) {
        it.menu.clear()
        it.title = destination.label
    }
}