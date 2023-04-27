package com.listgithubusersinglescreen.data.local.room.db

import com.listgithubusersinglescreen.BuildConfig
import com.listgithubusersinglescreen.data.local.entity.ComponentFollow
import com.listgithubusersinglescreen.data.local.entity.ComponentUser

const val oldVersion = 1
const val currentVersion = 2

/**
 * Create USER_TBL
 */
// nb: boolean in room == integer in sqlite
val CREATE_USER_TBL = """
    CREATE TABLE IF NOT EXISTS ${BuildConfig.USER_TBL_NEW} (
        ${ComponentUser.nodeId} TEXT PRIMARY KEY NOT NULL,
        ${ComponentUser.login} TEXT NOT NULL,
        ${ComponentUser.followers} INTEGER NOT NULL,
        ${ComponentUser.avatar_url} TEXT NOT NULL,
        ${ComponentUser.following} INTEGER NOT NULL,
        ${ComponentUser.name} TEXT NOT NULL,
        ${ComponentUser.url} TEXT NOT NULL,
        ${ComponentUser.loved} INTEGER NOT NULL
        )
""".trimIndent()



/**
 * Create FOLLOW_TBL
 */
val CREATE_FOLLOW_TBL = """
    CREATE TABLE IF NOT EXISTS ${BuildConfig.FOLLOW_TBL_NEW} (
        ${ComponentFollow.id} INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
        ${ComponentFollow.nodeId_user} TEXT NOT NULL, 
        ${ComponentFollow.nodeId} TEXT NOT NULL, 
        ${ComponentFollow.login} TEXT NOT NULL, 
        ${ComponentFollow.followers} INTEGER NOT NULL, 
        ${ComponentFollow.avatar_url} TEXT NOT NULL, 
        ${ComponentFollow.following} INTEGER NOT NULL, 
        ${ComponentFollow.name} TEXT NOT NULL, 
        ${ComponentFollow.url} TEXT NOT NULL, 
        ${ComponentFollow.follow_status} TEXT NOT NULL,
        FOREIGN KEY(${ComponentFollow.nodeId_user}) REFERENCES ${BuildConfig.USER_TBL_NEW}(${ComponentUser.nodeId}) ON DELETE CASCADE
        )
""".trimIndent()



/**
 * INSERT OLD DATA TO NEW_USER_TBL
 */
val INSERT_OLD_DATA_TO_NEW_USER_TBL = """
    INSERT INTO ${BuildConfig.USER_TBL_NEW} (${ComponentUser.nodeId}, ${ComponentUser.login}, ${ComponentUser.followers}, ${ComponentUser.avatar_url}, ${ComponentUser.following}, ${ComponentUser.name}, ${ComponentUser.url}, ${ComponentUser.loved})
    SELECT nodeId, login, followers, avatar_url, following, name, url, loved FROM ${BuildConfig.USER_TBL_OLD}
""".trimIndent()



/**
 * DROP OLD_USER_TBL IF EXIST
 */
val DROP_TABLE_USER_IF_EXIST = """
    DROP TABLE IF EXISTS ${BuildConfig.USER_TBL_OLD}
""".trimIndent()
