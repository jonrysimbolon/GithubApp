package com.listgithubusersinglescreen.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.listgithubusersinglescreen.repository.user.Repository

class HomeViewModel(
    private val userRepository: Repository
) : ViewModel() {

    val searchQuery = MutableLiveData<String>()

    /*private val _searchViewExpanded = MutableLiveData(false)
    private val _searchText = MutableLiveData<String>()

    val searchViewExpanded: LiveData<Boolean>
        get() = _searchViewExpanded

    fun setSearchExpand(visible: Boolean){
        _searchViewExpanded.value = visible
    }

    val searchText: LiveData<String>
        get() = _searchText
    fun setSearchText(text: String) {
        _searchText.value = text
    }*/
    fun getFreshUser() = userRepository.getUsers(null)
    fun getSearchUser(login: String) = userRepository.getUsers(login)
}