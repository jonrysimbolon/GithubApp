package com.listgithubusersinglescreen.repository.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.listgithubusersinglescreen.BuildConfig
import com.listgithubusersinglescreen.helper.ListTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesParent {

    private val notUseSystemKey = booleanPreferencesKey(BuildConfig.NOT_USE_SYSTEM)
    private val themeKey = intPreferencesKey(BuildConfig.THEME_SETTINGS)

    override fun getThemeSetting(): Flow<ListTheme> {
        return dataStore.data.map { preferences ->
            when(preferences[notUseSystemKey]){
                true -> {
                    when(preferences[themeKey]){
                        0 -> ListTheme.DAY
                        1 -> ListTheme.NIGHT
                        else -> ListTheme.UNKNOWN
                    }
                }
                false -> ListTheme.UNKNOWN
                else -> ListTheme.UNKNOWN
            }
        }
    }

    override suspend fun saveThemeSetting(isDarkModeActive: ListTheme) {
        dataStore.edit { preferences ->
            preferences[themeKey] = when(isDarkModeActive){
                ListTheme.DAY -> 0
                ListTheme.NIGHT -> 1
                ListTheme.UNKNOWN -> 2
            }
        }
    }

    override suspend fun saveNotUseThemeSetting(isNotSystem: Boolean) {
        dataStore.edit { preferences ->
            preferences[notUseSystemKey] = isNotSystem
        }
    }
}