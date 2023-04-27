package com.listgithubusersinglescreen.repository.follow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.listgithubusersinglescreen.data.local.entity.FollowEntity
import com.listgithubusersinglescreen.data.local.room.FollowDao
import com.listgithubusersinglescreen.data.remote.retrofit.ApiService
import com.listgithubusersinglescreen.helper.FollowType
import com.listgithubusersinglescreen.helper.ResultStatus

class FollowRepositoryImpl constructor(
    private val remoteDataSource: ApiService,
    private val localDataSource: FollowDao
) : FollowRepository {

    override fun getFollowersUsers(
        user: String,
        nodeId: String
    ): LiveData<ResultStatus<List<FollowEntity>>> =
        liveData {
            emit(ResultStatus.Loading)
            try {
                val response = remoteDataSource.getFollowersUsers(user)

                val followerList = response.map { follower ->
                    FollowEntity(
                        nodeIdUser = nodeId,
                        nodeId = follower.nodeId,
                        login = follower.login ?: "",
                        followers = follower.followers ?: 0,
                        avatarUrl = follower.avatarUrl ?: "",
                        following = follower.following ?: 0,
                        name = follower.name ?: "",
                        url = follower.url ?: "",
                        follow_status = FollowType.Follower
                    )
                }
                localDataSource.insertFollows(followerList)
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
                emit(ResultStatus.Error(e.message.toString()))
            }

            val localData: LiveData<ResultStatus<List<FollowEntity>>> =
                localDataSource.getFollowers(nodeId).map {
                    ResultStatus.Success(it)
                }
            emitSource(localData)
        }

    override fun getFollowingUsers(
        user: String,
        nodeId: String
    ): LiveData<ResultStatus<List<FollowEntity>>> =
        liveData {
            emit(ResultStatus.Loading)
            try {
                val response = remoteDataSource.getFollowingUsers(user)

                val followingList = response.map { following ->
                    FollowEntity(
                        nodeIdUser = nodeId,
                        nodeId = following.nodeId,
                        login = following.login ?: "",
                        followers = following.followers ?: 0,
                        avatarUrl = following.avatarUrl ?: "",
                        following = following.following ?: 0,
                        name = following.name ?: "",
                        url = following.url ?: "",
                        follow_status = FollowType.Following
                    )
                }
                localDataSource.insertFollows(followingList)
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
                emit(ResultStatus.Error(e.message.toString()))
            }

            val localData: LiveData<ResultStatus<List<FollowEntity>>> =
                localDataSource.getFollowings(nodeId).map {
                    ResultStatus.Success(it)
                }
            emitSource(localData)
        }

    companion object {
        val TAG = FollowRepositoryImpl::class.simpleName
    }
}