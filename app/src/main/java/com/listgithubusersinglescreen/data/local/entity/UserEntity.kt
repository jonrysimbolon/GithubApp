package com.listgithubusersinglescreen.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.listgithubusersinglescreen.BuildConfig

@Entity(tableName = BuildConfig.USER_TBL_NEW)
class UserEntity(
    @field:ColumnInfo(name = ComponentUser.nodeId)
    @field:PrimaryKey
    val nodeId: String,

    @field:ColumnInfo(name = ComponentUser.login)
    val login: String,

    @field:ColumnInfo(name = ComponentUser.followers)
    val followers: Int,

    @field:ColumnInfo(name = ComponentUser.avatar_url)
    val avatarUrl: String,

    @field:ColumnInfo(name = ComponentUser.following)
    val following: Int,

    @field:ColumnInfo(name = ComponentUser.name)
    val name: String,

    @field:ColumnInfo(name = ComponentUser.url)
    val url: String,

    @field:ColumnInfo(name = ComponentUser.loved)
    var isLoved: Boolean = false
)

/**
 *
 * name of table set on build.gradle
 *
 *  version  |         name        |
 *    1      |       user_tbl      | id | nodeId_user | nodeId | login | followers | avatar_url | following | name | url | follow_status |
 *    2      |      user_01_tbl    | id | nodeId_user | nodeId | login | followers | avatar_url | following | name | url | follow_status |
 *
 *
 *
 *
 *
 */

class ComponentUser{
    companion object {
        const val nodeId: String = "nodeId"
        const val login: String = "login"
        const val followers: String = "followers"
        const val avatar_url: String = "avatar_url"
        const val following: String = "following"
        const val name: String = "name"
        const val url: String = "url"
        const val loved: String = "loved"
    }
}