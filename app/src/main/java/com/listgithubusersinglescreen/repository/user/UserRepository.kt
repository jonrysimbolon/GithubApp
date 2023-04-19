package com.listgithubusersinglescreen.repository.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.listgithubusersinglescreen.helper.ResultStatus
import com.listgithubusersinglescreen.data.local.entity.UserEntity
import com.listgithubusersinglescreen.data.local.room.UserDao
import com.listgithubusersinglescreen.data.remote.retrofit.ApiService

class UserRepository constructor(
    private val remoteDataSource: ApiService,
    private val localDataSource: UserDao,
): Repository {

    override fun getFavoriteUser(): LiveData<ResultStatus<List<UserEntity>>> = liveData {
        emit(ResultStatus.Loading)
        try{
            val localData: LiveData<ResultStatus<List<UserEntity>>> = localDataSource.getFavUser().map {
                ResultStatus.Success(it)
            }
            emitSource(localData)
        }catch (e: Exception){
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

    override fun getUser(login: String, nodeId: String): LiveData<ResultStatus<UserEntity>> = liveData {
        emit(ResultStatus.Loading)
        try {
            val user = remoteDataSource.getUser(login)
            val userExist = localDataSource.isUserExist(nodeId)
            val userExistWithLoved = localDataSource.isUserExistWithLoved(nodeId)
            if (userExist) {
                if(userExistWithLoved){
                    localDataSource.updateUser(
                        UserEntity(
                            user.nodeId,
                            user.login ?: "",
                            user.followers ?: 0,
                            user.avatarUrl ?: "",
                            user.following ?: 0,
                            user.name ?: "",
                            user.url ?: "",
                            true
                        )
                    )
                }else {
                    localDataSource.updateUser(
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

        val localData: LiveData<ResultStatus<UserEntity>> = localDataSource.getUser(nodeId).map {
            ResultStatus.Success(it)
        }
        emitSource(localData)
    }

    override fun getUsers(login: String?): LiveData<ResultStatus<List<UserEntity>>> = liveData {
        emit(ResultStatus.Loading)
        try {

            val response = if (login != null) {
                remoteDataSource.searchUser(login).items
            } else {
                remoteDataSource.getUsers()
            }

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

            localDataSource.deleteAllFromFavorite()
            localDataSource.insertUsers(userNotExist)

        } catch (e: Exception) {
            Log.e(
                "UserRepository",
                "${if (login != null) "searchUser" else "freshUser"} : ${e.message.toString()}"
            )
            emit(ResultStatus.Error(e.message.toString()))
        }

        val localData: LiveData<ResultStatus<List<UserEntity>>> =
            if (login != null) {
                localDataSource.getUserSearch(login).map {
                    ResultStatus.Success(it)
                }
            } else {
                localDataSource.getUsers().map {
                    ResultStatus.Success(it)
                }
            }
        emitSource(localData)
    }
}