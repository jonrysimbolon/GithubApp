package com.listgithubusersinglescreen.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.listgithubusersinglescreen.repository.user.UserRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    fun getUserFavorite() = userRepository.getFavoriteUser()
    fun setLovedUser(nodeId: String, lovedState: Boolean){
        viewModelScope.launch {
            userRepository.setLovedUser(nodeId, lovedState)
        }
    }
}