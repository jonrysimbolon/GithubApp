package com.listgithubusersinglescreen.data.remote.response

import com.google.gson.annotations.SerializedName

data class User(
    @field:SerializedName("node_id")
    val nodeId: String,

    @field:SerializedName("login")
    val login: String? = null,

    @field:SerializedName("followers")
    val followers: Int? = null,

    @field:SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @field:SerializedName("following")
    val following: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("html_url")
    val url: String? = null,

)
