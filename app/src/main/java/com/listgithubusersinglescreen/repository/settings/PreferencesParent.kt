package com.listgithubusersinglescreen.repository.settings

import com.listgithubusersinglescreen.helper.ListTheme
import kotlinx.coroutines.flow.Flow

interface PreferencesParent {
    fun getThemeSetting(): Flow<ListTheme>
    suspend fun saveThemeSetting(isDarkModeActive: ListTheme)
    suspend fun saveNotUseThemeSetting(isNotSystem: Boolean)
}