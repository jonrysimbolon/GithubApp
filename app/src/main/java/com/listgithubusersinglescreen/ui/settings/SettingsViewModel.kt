package com.listgithubusersinglescreen.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.listgithubusersinglescreen.helper.ListTheme
import com.listgithubusersinglescreen.repository.settings.PreferencesParent
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val pref: PreferencesParent
): ViewModel() {

    fun saveNotUseThemeSetting(isNotSystem: Boolean){
        viewModelScope.launch {
            pref.saveNotUseThemeSetting(isNotSystem)
        }
    }
    fun saveThemeSetting(isDarkModeActive: ListTheme){
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}