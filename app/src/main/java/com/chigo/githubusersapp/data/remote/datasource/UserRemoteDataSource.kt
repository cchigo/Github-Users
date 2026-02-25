package com.chigo.githubusersapp.data.remote.datasource

import com.chigo.githubusersapp.data.remote.model.UserDetailDto
import com.chigo.githubusersapp.data.remote.model.UserDto

interface UserRemoteDataSource {
    suspend fun getUsers(since: Int): List<UserDto>
    suspend fun getUserDetail(username: String): UserDetailDto
}