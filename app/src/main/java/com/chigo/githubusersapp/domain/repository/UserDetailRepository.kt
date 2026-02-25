package com.chigo.githubusersapp.domain.repository

import com.chigo.githubusersapp.domain.model.UserDetail
import com.chigo.githubusersapp.data.util.BaseResponse
import kotlinx.coroutines.flow.Flow

interface UserDetailRepository {
    fun getUserDetail(username: String): Flow<BaseResponse<UserDetail>>
}