package com.listgithubusersinglescreen.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.listgithubusersinglescreen.BuildConfig
import com.listgithubusersinglescreen.data.local.room.UserDatabase
import com.listgithubusersinglescreen.data.remote.retrofit.ApiService
import com.listgithubusersinglescreen.repository.settings.PreferencesParent
import com.listgithubusersinglescreen.repository.settings.SettingPreferences
import com.listgithubusersinglescreen.repository.user.Repository
import com.listgithubusersinglescreen.repository.user.UserRepository
import com.listgithubusersinglescreen.ui.home.HomeViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val loggingInterceptor = with(HttpLoggingInterceptor()) {
    if (BuildConfig.DEBUG) {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        setLevel(HttpLoggingInterceptor.Level.NONE)
    }
}

private val client = with(OkHttpClient.Builder()) {
    addInterceptor { chain ->
        val request = chain.request().newBuilder().addHeader(
            "Authorization", "token ${BuildConfig.KEY}"
        ).build()
        chain.proceed(request)
    }
    addInterceptor(loggingInterceptor)
    build()
}

private val retrofit = with(Retrofit.Builder()) {
    baseUrl("https://api.github.com")
    addConverterFactory(GsonConverterFactory.create())
    client(client)
    build()
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val appModule = module {

    single {
        retrofit.create(ApiService::class.java)
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            UserDatabase::class.java,
            "User.db"
        ).build()
    }

    single {
        get<UserDatabase>().userDao()
    }

    single {
        androidContext().dataStore
    }

    single<PreferencesParent> {
        SettingPreferences(get())
    }

    single<Repository> {
        UserRepository(get(), get())
    }

    viewModel {
        HomeViewModel(get(), get())
    }

}
