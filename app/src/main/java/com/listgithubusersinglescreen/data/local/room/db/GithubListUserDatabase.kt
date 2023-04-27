package com.listgithubusersinglescreen.data.local.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.listgithubusersinglescreen.BuildConfig
import com.listgithubusersinglescreen.data.local.entity.FollowEntity
import com.listgithubusersinglescreen.data.local.entity.UserEntity
import com.listgithubusersinglescreen.data.local.room.FollowDao
import com.listgithubusersinglescreen.data.local.room.UserDao

const val oldVersion = 1
const val currentVersion = 2

@Database(
    entities = [UserEntity::class, FollowEntity::class],
    version = currentVersion,
    exportSchema = false
)
abstract class GithubListUserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun followDao(): FollowDao

}


// boolean in room == integer in sqlite
private val CREATE_USER_TBL = """
    CREATE TABLE IF NOT EXISTS ${BuildConfig.USER_TBL_NEW} (
        nodeId TEXT PRIMARY KEY NOT NULL,
        login TEXT NOT NULL,
        followers INTEGER NOT NULL,
        avatar_url TEXT NOT NULL,
        following INTEGER NOT NULL,
        name TEXT NOT NULL,
        url TEXT NOT NULL,
        loved INTEGER NOT NULL
        )
""".trimIndent()

private val CREATE_FOLLOW_TBL = """
    CREATE TABLE IF NOT EXISTS ${BuildConfig.FOLLOW_TBL_NEW} (
        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
        nodeId_user TEXT NOT NULL, 
        nodeId TEXT NOT NULL, 
        login TEXT NOT NULL, 
        followers INTEGER NOT NULL, 
        avatar_url TEXT NOT NULL, 
        following INTEGER NOT NULL, 
        name TEXT NOT NULL, 
        url TEXT NOT NULL, 
        follow_status TEXT NOT NULL,
        FOREIGN KEY(nodeId_user) REFERENCES ${BuildConfig.USER_TBL_NEW}(nodeId) ON DELETE CASCADE
        )
""".trimIndent()

private val INSERT_OLD_DATA_TO_NEW_USER_TBL = """
    INSERT INTO ${BuildConfig.USER_TBL_NEW} (nodeId, login, followers, avatar_url, following, name, url, loved)
    SELECT nodeId, login, followers, avatar_url, following, name, url, loved FROM user_tbl
""".trimIndent()

private val DROP_TABLE_USER_IF_EXIST = """
    DROP TABLE IF EXISTS ${BuildConfig.USER_TBL_OLD}
""".trimIndent()


val migration = object : Migration(oldVersion, currentVersion) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create a new table with the desired schema
        db.execSQL(CREATE_USER_TBL)
        db.execSQL(CREATE_FOLLOW_TBL)

        // Copy data from old table to new table
        db.execSQL(INSERT_OLD_DATA_TO_NEW_USER_TBL)

        // Drop the old table
        db.execSQL(DROP_TABLE_USER_IF_EXIST)
    }
}
