package com.listgithubusersinglescreen.repository.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.listgithubusersinglescreen.data.local.entity.UserEntity
import com.listgithubusersinglescreen.data.local.room.UserDao
import com.listgithubusersinglescreen.data.remote.retrofit.ApiService
import com.listgithubusersinglescreen.helper.ResultStatus

class UserRepositoryImpl constructor(
    private val remoteDataSource: ApiService,
    private val localDataSource: UserDao
) : UserRepository {

    override fun getFavoriteUser(): LiveData<ResultStatus<List<UserEntity>>> = liveData {
        emit(ResultStatus.Loading)
        try {
            val localData: LiveData<ResultStatus<List<UserEntity>>> =
                localDataSource.getFavUser().map {
                    ResultStatus.Success(it)
                }
            emitSource(localData)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ResultStatus.Error(e.message.toString()))
        }
    }

    override suspend fun setLovedUser(nodeId: String, lovedState: Boolean) {
        localDataSource.updateLoveUser(nodeId, lovedState)
    }

    override fun isLovedUser(nodeId: String): LiveData<Boolean> = liveData {
        emitSource(localDataSource.isLoved(nodeId))
    }

    override fun getUser(login: String, nodeId: String): LiveData<ResultStatus<UserEntity>> =
        liveData {
            emit(ResultStatus.Loading)
            try {
                val user = remoteDataSource.getUser(login)
                val userExist = localDataSource.isUserExist(nodeId)
                val userExistWithLoved = localDataSource.isUserExistWithLoved(nodeId)
                if (userExist) {
                    localDataSource.updateUser(
                        UserEntity(
                            user.nodeId,
                            user.login ?: "",
                            user.followers ?: 0,
                            user.avatarUrl ?: "",
                            user.following ?: 0,
                            user.name ?: "",
                            user.url ?: "",
                            userExistWithLoved
                        )
                    )
                } else {
                    localDataSource.insertUser(
                        UserEntity(
                            user.nodeId,
                            user.login ?: "",
                            user.followers ?: 0,
                            user.avatarUrl ?: "",
                            user.following ?: 0,
                            user.name ?: "",
                            user.url ?: "",
                            false
                        )
                    )
                }

            } catch (e: Exception) {
                Log.d("UserRepository", "getUser: ${e.message.toString()}")
                emit(ResultStatus.Error(e.message.toString()))
            }

            val localData: LiveData<ResultStatus<UserEntity>> =
                localDataSource.getUser(nodeId).map {
                    ResultStatus.Success(it)
                }
            emitSource(localData)
        }

    override suspend fun getSearchUsers(login: String): List<UserEntity> {
        // TODO: get search list from local
        // TODO: if empty...
        // TODO: ...search from server
        // TODO: if server get data
        // TODO: store to local

        val response = remoteDataSource.searchUser(login).items
        val listExistNodeId = localDataSource.getLovedNodeId()
        val userList = response.map { user ->
            UserEntity(
                user.nodeId,
                user.login ?: "",
                user.followers ?: 0,
                user.avatarUrl ?: "",
                user.following ?: 0,
                user.name ?: "",
                user.url ?: "",
                false
            )
        }
        val userNotExist = userList.filter { userEntity ->
            !listExistNodeId.contains(userEntity.nodeId)
        }
        localDataSource.deleteUsersExceptFav()
        localDataSource.insertUsers(userNotExist)
        return localDataSource.getUserSearch(login)
    }

    override fun getUsers(): LiveData<ResultStatus<List<UserEntity>>> =
        
        // TODO: get list from local
        // TODO: if empty...
        // TODO: ...get from server
        // TODO: if server get data
        // TODO: store to local

        liveData {
            emit(ResultStatus.Loading)
            try {
                val response = remoteDataSource.getUsers()
                val listExistNodeId = localDataSource.getLovedNodeId()
                val userList = response.map { user ->
                    UserEntity(
                        user.nodeId,
                        user.login ?: "",
                        user.followers ?: 0,
                        user.avatarUrl ?: "",
                        user.following ?: 0,
                        user.name ?: "",
                        user.url ?: "",
                        false
                    )
                }
                val userNonFav = userList.filter { userEntity ->
                    !listExistNodeId.contains(userEntity.nodeId)
                }
                localDataSource.deleteUsersExceptFav()
                localDataSource.insertUsers(userNonFav)
            } catch (e: Exception) {
                Log.e(
                    "UserRepository",
                    "freshUser : ${e.message.toString()}"
                )
                emit(ResultStatus.Error(e.message.toString()))
            }
            val localData: LiveData<ResultStatus<List<UserEntity>>> =
                localDataSource.getUsers().map {
                    ResultStatus.Success(it)
                }
            emitSource(localData)
        }
}