package com.chigo.githubusersapp.presentation.features.userdetail

import com.chigo.githubusersapp.domain.model.UserDetail

sealed class UserDetailState {
    object Idle : UserDetailState()
    object Loading : UserDetailState()
    data class Success(val user: UserDetail) : UserDetailState()
    data class Error(val message: String) : UserDetailState()
}

