package com.githubuser.data.remote.response

import com.google.gson.annotations.SerializedName

data class UsersSearch(
    @field:SerializedName("items")
    val items: List<User>
)