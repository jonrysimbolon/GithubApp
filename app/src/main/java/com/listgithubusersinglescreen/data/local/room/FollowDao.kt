package com.listgithubusersinglescreen.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.listgithubusersinglescreen.BuildConfig
import com.listgithubusersinglescreen.data.local.entity.FollowEntity
import com.listgithubusersinglescreen.helper.FollowType

@Dao
interface FollowDao {

    @Query("SELECT * from ${BuildConfig.FOLLOW_TBL_NEW} where nodeId_user = :nodeId AND follow_status = :status")
    fun getFollowers(nodeId: String, status: FollowType = FollowType.Follower): LiveData<List<FollowEntity>>

    @Query("SELECT * from ${BuildConfig.FOLLOW_TBL_NEW} where nodeId_user = :nodeId AND follow_status = :status")
    fun getFollowings(nodeId: String, status: FollowType = FollowType.Following): LiveData<List<FollowEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollows(user: List<FollowEntity>)

}