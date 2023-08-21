package com.listgithubusersinglescreen.repository.user

import androidx.lifecycle.LiveData
import com.listgithubusersinglescreen.data.local.entity.UserEntity
import com.listgithubusersinglescreen.helper.ResultStatus
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getFavoriteUser(): LiveData<ResultStatus<List<UserEntity>>>
    suspend fun setLovedUser(nodeId: String, lovedState: Boolean)
    fun isLovedUser(nodeId: String): LiveData<Boolean>
    fun getUser(login: String, nodeId: String): LiveData<ResultStatus<UserEntity>>
    fun getUsers(): Flow<List<UserEntity>>
    suspend fun getSearchUsers(login: String): List<UserEntity>
}