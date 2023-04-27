package com.listgithubusersinglescreen.ui.home

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.listgithubusersinglescreen.repository.user.UserRepository

class HomeViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _searchText = MutableLiveData<String>()
    val searchText: LiveData<String>
        get() = _searchText

    private val _searchView = MutableLiveData<SearchView>()

    val searchView: LiveData<SearchView>
        get() = _searchView

    fun setSearchText(text: String) {
        _searchText.value = text
    }

    fun setSearchView(searchView: SearchView){
        _searchView.value = searchView
    }

    fun getFreshUser() = userRepository.getUsers(null)
    fun getSearchUser(login: String) = userRepository.getUsers(login)
}