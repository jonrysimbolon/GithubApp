package com.listgithubusersinglescreen.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.listgithubusersinglescreen.data.local.entity.UserEntity
import com.listgithubusersinglescreen.helper.ResultStatus
import com.listgithubusersinglescreen.repository.user.UserRepository
import com.listgithubusersinglescreen.utils.DispatcherProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    var isSearch = MutableLiveData(false)

    private val _searchText = MutableLiveData<String>()
    val searchText: LiveData<String>
        get() = _searchText


    private val _users = MutableStateFlow<ResultStatus<List<UserEntity>>>(ResultStatus.Loading)
    val users: StateFlow<ResultStatus<List<UserEntity>>>
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
        viewModelScope.launch {
            _users.value = ResultStatus.Loading
            userRepository.getUsers()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _users.value = ResultStatus.Error(e.message.toString())
                }
                .collect { result ->
                    _users.value = ResultStatus.Success(result)
                }
        }
    }

    fun setSearchText(text: String){
        _searchText.value = text
    }
}