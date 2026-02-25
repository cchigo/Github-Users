package com.chigo.githubusersapp.data.remote.datasource

import com.chigo.githubusersapp.data.remote.api.GitHubApiService
import com.chigo.githubusersapp.data.remote.model.UserDetailDto
import com.chigo.githubusersapp.data.remote.model.UserDto
import com.chigo.githubusersapp.data.util.USERS_PER_PAGE
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val apiService: GitHubApiService
) : UserRemoteDataSource {

    override suspend fun getUsers(since: Int): List<UserDto> {
        return apiService.getUsers(
            since = since,
            perPage = USERS_PER_PAGE
        )
    }

    override suspend fun getUserDetail(username: String): UserDetailDto {
        return apiService.getUserDetail(username)
    }
}