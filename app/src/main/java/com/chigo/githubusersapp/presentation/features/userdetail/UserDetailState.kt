package com.chigo.githubusersapp.presentation.features.userdetail

import com.chigo.githubusersapp.domain.model.UserDetail

data class UserDetailState(
    val userDetail: UserDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)