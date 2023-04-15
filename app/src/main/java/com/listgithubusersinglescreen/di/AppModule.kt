package com.listgithubusersinglescreen.di

import com.listgithubusersinglescreen.BuildConfig
import com.listgithubusersinglescreen.data.remote.retrofit.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

/*
val getApiService: ApiService by lazy {
    retrofit.create(ApiService::class.java)
}*/


val appModule = module {
    single {
        /*Retrofit.Builder()
            .baseUrl("https://google.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(MyApi::class.java)*/
        retrofit.create(ApiService::class.java)
    }
    /*single<MainRepository> {
        MainRepositoryImpl(get())
    }
    viewModel {
        MainViewModel(get())
    }*/
}
