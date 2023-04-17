package com.listgithubusersinglescreen.di

import androidx.room.Room
import com.listgithubusersinglescreen.BuildConfig
import com.listgithubusersinglescreen.data.local.room.UserDatabase
import com.listgithubusersinglescreen.data.remote.retrofit.ApiService
import com.listgithubusersinglescreen.repository.user.Repository
import com.listgithubusersinglescreen.repository.user.UserRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
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


val appModule = module {
    single {
        retrofit.create(ApiService::class.java)
    }
    single {
        Room.databaseBuilder(
            androidApplication(),
            UserDatabase::class.java,
            "User.db"
        ).build()
    }
    single<Repository> {
        UserRepository(get(), get())
    }

}
