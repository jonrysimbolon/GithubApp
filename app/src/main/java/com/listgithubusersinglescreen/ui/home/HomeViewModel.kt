package com.listgithubusersinglescreen.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.listgithubusersinglescreen.data.local.entity.UserEntity
import com.listgithubusersinglescreen.helper.ResultStatus
import com.listgithubusersinglescreen.repository.user.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    var isSearch = MutableLiveData(false)

    private val _searchText = MutableLiveData<String>()
    val searchText: LiveData<String>
        get() = _searchText


    private val _users: LiveData<ResultStatus<List<UserEntity>>> = userRepository.getUsers()
    val users: LiveData<ResultStatus<List<UserEntity>>>
        get() = _users


    private var _searchUsers: MutableLiveData<ResultStatus<List<UserEntity>>> = MutableLiveData()
    val searchUsers: LiveData<ResultStatus<List<UserEntity>>>
        get() = _searchUsers


    fun getSearchUser(login: String){
        viewModelScope.launch {
            _searchUsers.value = ResultStatus.Loading
            try {
                val userSearchList = userRepository.getSearchUsers(login)
                _searchUsers.value = ResultStatus.Success(userSearchList)
            }catch (e: Exception){
                e.printStackTrace()
                _searchUsers.value = ResultStatus.Error(e.message.toString())
            }
        }
    }

    fun getFreshUser(){
        if(_users.value == null){
            userRepository.getUsers()
        }
    }

    fun setSearchText(text: String){
        _searchText.value = text
    }

}