package com.listgithubusersinglescreen.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.listgithubusersinglescreen.BuildConfig
import com.listgithubusersinglescreen.helper.FollowType

@Entity(tableName = BuildConfig.FOLLOW_TBL_NEW,
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = [ComponentUser.nodeId],
            childColumns = [ComponentFollow.nodeId_user],
            onDelete = ForeignKey.CASCADE
        )
    ])
class FollowEntity(

    @field:PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = ComponentFollow.id)
    val id: Int = 0,

    @field:ColumnInfo(name = ComponentFollow.nodeId_user)
    val nodeIdUser: String,

    @field:ColumnInfo(name = ComponentFollow.nodeId)
    val nodeId: String,

    @field:ColumnInfo(name = ComponentFollow.login)
    val login: String,

    @field:ColumnInfo(name = ComponentFollow.followers)
    val followers: Int,

    @field:ColumnInfo(name = ComponentFollow.avatar_url)
    val avatarUrl: String,

    @field:ColumnInfo(name = ComponentFollow.following)
    val following: Int,

    @field:ColumnInfo(name = ComponentFollow.name)
    val name: String,

    @field:ColumnInfo(name = ComponentFollow.url)
    val url: String,

    @field:ColumnInfo(name = ComponentFollow.follow_status)
    val follow_status: FollowType,
)

/**
 *
 * name of table set on build.gradle
 *
 *  version  |        name        |
 *    1      |        NULL        | NULL
 *    2      |     follow_tbl     | id | nodeId_user | nodeId | login | followers | avatar_url | following | name | url | follow_status |
 *
 *
 *
 *
 *
 */



class ComponentFollow{
    companion object {
        const val id: String = "id"
        const val nodeId_user: String = "nodeId_user"
        const val nodeId: String = "nodeId"
        const val login: String = "login"
        const val followers: String = "followers"
        const val avatar_url: String = "avatar_url"
        const val following: String = "following"
        const val name: String = "name"
        const val url: String = "url"
        const val follow_status: String = "follow_status"
    }
}