package com.chigo.githubusersapp.presentation.features.userdetail

import com.chigo.githubusersapp.domain.model.UserDetail

sealed class UserDetailState {
    object Idle : UserDetailState()
    object Loading : UserDetailState()
    data class Success(val user: UserDetail, val isRefreshing: Boolean = false) : UserDetailState()
    data class Error(val message: String) : UserDetailState()

    // this returns a cached user with error, i=eg, if the value exists in the the db and network fails
    data class CachedWithError(val user: UserDetail, val message: String) : UserDetailState()

}

