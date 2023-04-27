package com.listgithubusersinglescreen.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.listgithubusersinglescreen.BuildConfig
import com.listgithubusersinglescreen.data.local.room.db.GithubListUserDatabase
import com.listgithubusersinglescreen.data.remote.retrofit.ApiService
import com.listgithubusersinglescreen.repository.follow.FollowRepository
import com.listgithubusersinglescreen.repository.follow.FollowRepositoryImpl
import com.listgithubusersinglescreen.repository.settings.PreferencesParent
import com.listgithubusersinglescreen.repository.settings.SettingPreferences
import com.listgithubusersinglescreen.repository.user.UserRepository
import com.listgithubusersinglescreen.repository.user.UserRepositoryImpl
import com.listgithubusersinglescreen.ui.detail.DetailViewModel
import com.listgithubusersinglescreen.ui.home.HomeViewModel
import com.listgithubusersinglescreen.ui.main.MainViewModel
import com.listgithubusersinglescreen.ui.settings.SettingsViewModel
import com.listgithubusersinglescreen.ui.userfollow.UserFollowViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = BuildConfig.SETTINGS_PREF)

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

val appModule = module {

    single {
        retrofit.create(ApiService::class.java)
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            GithubListUserDatabase::class.java,
            BuildConfig.USER_DB
        )
            .build()
    }

    single {
        get<GithubListUserDatabase>().userDao()
    }

    single {
        androidContext().dataStore
    }

    single<PreferencesParent> {
        SettingPreferences(get())
    }

    single<UserRepository> {
        UserRepositoryImpl(get(), get())
    }

    single<FollowRepository> {
        FollowRepositoryImpl(get(), get())
    }

    viewModel {
        HomeViewModel(get())
    }

    viewModel {
        MainViewModel(get())
    }

    viewModel {
        SettingsViewModel(get())
    }

    viewModel {
        DetailViewModel(get())
    }

    viewModel {
        UserFollowViewModel(get())
    }

}
