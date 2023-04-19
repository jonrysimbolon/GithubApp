package com.listgithubusersinglescreen.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.listgithubusersinglescreen.helper.ListTheme
import com.listgithubusersinglescreen.repository.settings.PreferencesParent
import com.listgithubusersinglescreen.repository.user.Repository

class HomeViewModel(
    private val userRepository: Repository
) : ViewModel() {
    fun getFreshUser() = userRepository.getUsers(null)
    fun getSearchUser(login: String) = userRepository.getUsers(login)
}