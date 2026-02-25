package com.chigo.githubusersapp.domain.repository

import androidx.paging.PagingData
import com.chigo.githubusersapp.domain.model.User
import kotlinx.coroutines.flow.Flow


interface UserRepository {
    fun getUsers(): Flow<PagingData<User>>
}