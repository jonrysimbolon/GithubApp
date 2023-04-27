package com.listgithubusersinglescreen.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.listgithubusersinglescreen.BuildConfig

@Entity(tableName = BuildConfig.USER_TBL_NEW)
class UserEntity(
    @field:ColumnInfo(name = "nodeId")
    @field:PrimaryKey
    val nodeId: String,

    @field:ColumnInfo(name = "login")
    val login: String,

    @field:ColumnInfo(name = "followers")
    val followers: Int,

    @field:ColumnInfo(name = "avatar_url")
    val avatarUrl: String,

    @field:ColumnInfo(name = "following")
    val following: Int,

    @field:ColumnInfo(name = "name")
    val name: String,

    @field:ColumnInfo(name = "url")
    val url: String,

    @field:ColumnInfo(name = "loved")
    var isLoved: Boolean = false
)