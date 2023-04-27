package com.listgithubusersinglescreen.ui.follow

import androidx.lifecycle.ViewModel
import com.listgithubusersinglescreen.repository.follow.FollowRepository

class UserFollowViewModel(
    private val followRepository: FollowRepository
) : ViewModel() {
    fun fetchFollowers(user: String, nodeId: String) =
        followRepository.getFollowingUsers(user, nodeId)

    fun fetchFollowings(user: String, nodeId: String) =
        followRepository.getFollowingUsers(user, nodeId)
}