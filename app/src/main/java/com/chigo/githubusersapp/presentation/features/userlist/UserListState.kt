package com.chigo.githubusersapp.presentation.features.userlist

import androidx.paging.PagingData
import com.chigo.githubusersapp.domain.model.User


sealed class UserListState {
    object Idle : UserListState()
    object InitialLoading : UserListState()
    object Refreshing : UserListState()
    object Appending : UserListState()
    data class Success(val users: PagingData<User>) : UserListState()
    data class Error(val message: String) : UserListState()
    data class AppendError(val message: String) : UserListState()
}