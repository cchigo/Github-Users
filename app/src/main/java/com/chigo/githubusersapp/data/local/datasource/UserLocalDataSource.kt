package com.chigo.githubusersapp.data.local.datasource

import androidx.paging.PagingSource
import com.chigo.githubusersapp.data.local.model.UserEntity

interface UserLocalDataSource {

    fun getUsers(): PagingSource<Int, UserEntity>

    suspend fun getUserById(userId: Int): UserEntity?

    suspend fun getUserByUsername(username: String): UserEntity?

    suspend fun upsertUsers(users: List<UserEntity>)

    suspend fun clearUsers()
}