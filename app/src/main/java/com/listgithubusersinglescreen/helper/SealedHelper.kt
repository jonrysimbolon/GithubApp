package com.listgithubusersinglescreen.helper

sealed class ListTheme private constructor(){
    object DAY: ListTheme() // 0
    object NIGHT: ListTheme() // 1
    object UNKNOWN: ListTheme() // 2
}

sealed class ResultStatus<out R> private constructor(){
    data class Success<out T>(val data: T): ResultStatus<T>()
    data class Error(val error: String): ResultStatus<Nothing>()
    object Loading: ResultStatus<Nothing>()
}

enum class FollowType {
    Follower, Following
}
