package com.listgithubusersinglescreen.ui.home

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.listgithubusersinglescreen.repository.user.Repository

class HomeViewModel(
    private val userRepository: Repository
) : ViewModel() {

    val searchText = MutableLiveData<String>()
    val searchView = MutableLiveData<SearchView>()

    fun getFreshUser() = userRepository.getUsers(null)
    fun getSearchUser(login: String) = userRepository.getUsers(login)
}