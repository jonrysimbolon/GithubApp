package com.listgithubusersinglescreen.data.remote.response

import com.google.gson.annotations.SerializedName
import com.listgithubusersinglescreen.data.remote.response.User

data class UsersSearch(
    @field:SerializedName("items")
    val items: List<User>
)