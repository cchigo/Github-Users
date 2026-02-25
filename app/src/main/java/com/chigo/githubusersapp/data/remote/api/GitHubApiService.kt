package com.chigo.githubusersapp.data.remote.api

import com.chigo.githubusersapp.data.remote.model.UserDetailDto
import com.chigo.githubusersapp.data.remote.model.UserDto
import com.chigo.githubusersapp.data.util.USERS_PER_PAGE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface GitHubApiService {

    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int = USERS_PER_PAGE
    ): List<UserDto>

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String
    ): UserDetailDto
}