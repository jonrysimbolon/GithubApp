package com.listgithubusersinglescreen

import com.githubuser.data.remote.response.User
import com.githubuser.data.remote.response.UsersSearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("users/{user}/following")
    fun getFollowingUsers(@Path("user") user: String): Call<List<User>>

    @GET("users/{user}/followers")
    fun getFollowersUsers(@Path("user") user: String): Call<List<User>>

    @GET("users/{user}")
    suspend fun getUser(@Path("user") user: String): User

    @GET("/search/users")
    suspend fun searchUser(@Query("q") user: String): UsersSearch

}