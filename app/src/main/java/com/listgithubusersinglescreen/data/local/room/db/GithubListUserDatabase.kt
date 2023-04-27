package com.listgithubusersinglescreen.data.local.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.listgithubusersinglescreen.data.local.entity.FollowEntity
import com.listgithubusersinglescreen.data.local.entity.UserEntity
import com.listgithubusersinglescreen.data.local.room.FollowDao
import com.listgithubusersinglescreen.data.local.room.UserDao

@Database(
    entities = [UserEntity::class, FollowEntity::class],
    version = currentVersion,
    exportSchema = false
)
abstract class GithubListUserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun followDao(): FollowDao

}

val migration = object : Migration(oldVersion, currentVersion) {
    override fun migrate(db: SupportSQLiteDatabase) {

        /** (1) CREATE A NEW TABLE WITH THE DESIRED SCHEMA
         *      :.  Create user tbl
         *      :.  create follow tbl
         */
        db.execSQL(CREATE_USER_TBL)
        db.execSQL(CREATE_FOLLOW_TBL)


        /** (2) COPY DATA FROM OLD TABLE TO NEW TABLE
         *      :.  Copy data from old user tbl to new user tbl
         */
        db.execSQL(INSERT_OLD_DATA_TO_NEW_USER_TBL)


        /** (3) DROP THE OLD TABLE
         *      :.  Drop old user tbl if exist
         */
        db.execSQL(DROP_TABLE_USER_IF_EXIST)
    }
}
