package com.listgithubusersinglescreen.repository.settings

import kotlinx.coroutines.flow.Flow

interface PreferencesParent {
    fun getThemeSetting(): Flow<Boolean>
    suspend fun saveThemeSetting(isDarkModeActive: Boolean)
}