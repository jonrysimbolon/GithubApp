package com.listgithubusersinglescreen.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.listgithubusersinglescreen.BuildConfig
import com.listgithubusersinglescreen.R
import com.listgithubusersinglescreen.helper.ListTheme
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val viewModel: SettingsViewModel by sharedViewModel()
    private lateinit var notUseSystemPreference: SwitchPreferenceCompat
    private lateinit var themeSettingPreference: SwitchPreferenceCompat

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        notUseSystemPreference =
            findPreference<SwitchPreferenceCompat>(resources.getString(R.string.notUseSystemKey)) as SwitchPreferenceCompat
        themeSettingPreference =
            findPreference<SwitchPreferenceCompat>(resources.getString(R.string.themeSettingKey)) as SwitchPreferenceCompat
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == BuildConfig.NOT_USE_SYSTEM) {
            val notUseSystemTheme = sharedPreferences.getBoolean(BuildConfig.NOT_USE_SYSTEM, false)
            viewModel.saveNotUseThemeSetting(notUseSystemTheme)
        }

        if (key == BuildConfig.THEME_SETTINGS) {
            val themeValue = sharedPreferences.getBoolean(BuildConfig.THEME_SETTINGS, false)
            viewModel.saveThemeSetting(
                when (themeValue) {
                    true -> ListTheme.NIGHT
                    false -> ListTheme.DAY
                }
            )
        }
    }

}