package com.listgithubusersinglescreen.repository.follow

import androidx.lifecycle.LiveData
import com.listgithubusersinglescreen.data.local.entity.FollowEntity
import com.listgithubusersinglescreen.helper.ResultStatus

interface FollowRepository {
    fun getFollowersUsers(user: String, nodeId: String): LiveData<ResultStatus<List<FollowEntity>>>
    fun getFollowingUsers(user: String, nodeId: String): LiveData<ResultStatus<List<FollowEntity>>>
}