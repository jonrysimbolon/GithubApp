package com.listgithubusersinglescreen.repository.user

import androidx.lifecycle.LiveData
import com.listgithubusersinglescreen.helper.ResultStatus
import com.listgithubusersinglescreen.data.local.entity.UserEntity

interface UserRepository {
    fun getFavoriteUser(): LiveData<ResultStatus<List<UserEntity>>>
    suspend fun setLovedUser(nodeId: String, lovedState: Boolean)
    fun isLovedUser(nodeId: String): LiveData<Boolean>
    fun getUser(login: String, nodeId: String): LiveData<ResultStatus<UserEntity>>
    fun getUsers(login: String?): LiveData<ResultStatus<List<UserEntity>>>
}