package com.listgithubusersinglescreen.data.local.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.listgithubusersinglescreen.data.local.entity.UserEntity
import com.listgithubusersinglescreen.data.local.room.UserDao

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GithubListUserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
