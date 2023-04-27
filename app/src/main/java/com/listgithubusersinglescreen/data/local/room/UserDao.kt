package com.listgithubusersinglescreen.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.listgithubusersinglescreen.BuildConfig
import com.listgithubusersinglescreen.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM ${BuildConfig.USER_TBL_NEW} where nodeId = :nodeId")
    fun getUser(nodeId: String): LiveData<UserEntity>

    @Query("SELECT * FROM ${BuildConfig.USER_TBL_NEW}")
    fun getUsers(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM ${BuildConfig.USER_TBL_NEW} where login like '%' || :login || '%'")
    fun getUserSearch(login: String): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(user: List<UserEntity>)

    @Insert
    suspend fun insertUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("Update ${BuildConfig.USER_TBL_NEW} set loved = :stateLove where nodeId = :nodeId")
    suspend fun updateLoveUser(nodeId: String, stateLove: Boolean)

    @Query("DELETE FROM ${BuildConfig.USER_TBL_NEW} WHERE loved = 0")
    suspend fun deleteAllFromFavorite()

    @Query("SELECT nodeId FROM ${BuildConfig.USER_TBL_NEW} where loved = 1")
    suspend fun getLovedNodeId(): List<String>

    @Query("SELECT * FROM ${BuildConfig.USER_TBL_NEW} where loved = 1")
    fun getFavUser(): LiveData<List<UserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM ${BuildConfig.USER_TBL_NEW} WHERE nodeId = :nodeId AND loved = 1)")
    fun isLoved(nodeId: String): LiveData<Boolean>

    @Query("SELECT EXISTS(SELECT * FROM ${BuildConfig.USER_TBL_NEW} WHERE nodeId = :nodeId)")
    suspend fun isUserExist(nodeId: String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM ${BuildConfig.USER_TBL_NEW} WHERE nodeId = :nodeId AND loved = 1)")
    suspend fun isUserExistWithLoved(nodeId: String): Boolean


}