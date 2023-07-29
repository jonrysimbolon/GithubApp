package com.jonrysimbolon.settings.settings.di

import com.jonrysimbolon.settings.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

private val settingsModule = module {
    viewModel {
        SettingsViewModel(get())
    }
}

private val loadFeatures by lazy {
    loadKoinModules(settingsModule)
}

private val unloadFeatures by lazy {
    unloadKoinModules(settingsModule)
}

fun injectFeatures() = loadFeatures
fun unInjectFeature() = unloadFeatures
