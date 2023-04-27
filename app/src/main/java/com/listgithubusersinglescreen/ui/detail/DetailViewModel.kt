package com.listgithubusersinglescreen.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.listgithubusersinglescreen.repository.user.UserRepository
import kotlinx.coroutines.launch

class DetailViewModel(
    private val userRepository: UserRepository,
): ViewModel() {
    fun getUser(login: String, nodeId: String) = userRepository.getUser(login, nodeId)
    fun isLovedUser(nodeId: String) = userRepository.isLovedUser(nodeId)
    fun setLovedUser(nodeId: String, lovedState: Boolean){
        viewModelScope.launch {
            userRepository.setLovedUser(nodeId, lovedState)
        }
    }
}