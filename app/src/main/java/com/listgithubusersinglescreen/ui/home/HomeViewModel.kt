package com.listgithubusersinglescreen.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.listgithubusersinglescreen.repository.settings.PreferencesParent
import com.listgithubusersinglescreen.repository.user.Repository

class HomeViewModel(
    private val userRepository: Repository,
    private val pref: PreferencesParent
) : ViewModel() {
    fun getFreshUser() = userRepository.getUsers(null)
    fun getSearchUser(login: String) = userRepository.getUsers(login)
    fun getThemeSettings(): LiveData<Boolean> = pref.getThemeSetting().asLiveData()
}