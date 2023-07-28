package com.jonrysimbolon.settings.settings.di

import com.jonrysimbolon.settings.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val settingsModule = module {
    viewModel {
        SettingsViewModel(get())
    }
}

private val loadFeatures by lazy {
    loadKoinModules(settingsModule)
}

fun injectFeatures() = loadFeatures
