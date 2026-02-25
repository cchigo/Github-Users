package com.chigo.githubusersapp.data.repository

import com.chigo.githubusersapp.data.local.datasource.UserLocalDataSource
import com.chigo.githubusersapp.data.local.mapper.toUserDetail
import com.chigo.githubusersapp.data.remote.mapper.toDomain
import com.chigo.githubusersapp.data.remote.datasource.UserRemoteDataSource
import com.chigo.githubusersapp.data.remote.mapper.toUserEntity
import com.chigo.githubusersapp.data.remote.model.UserDetailDto
import com.chigo.githubusersapp.domain.model.UserDetail
import com.chigo.githubusersapp.domain.repository.UserDetailRepository
import com.chigo.githubusersapp.data.util.BaseResponse
import com.chigo.githubusersapp.data.util.GeneralErrorHandler
import com.chigo.githubusersapp.data.util.NetworkChecker
import com.chigo.githubusersapp.data.util.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserDetailRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource,
    private val networkChecker: NetworkChecker,
    private val errorHandler: GeneralErrorHandler
) : UserDetailRepository {

    override fun getUserDetail(username: String): Flow<BaseResponse<UserDetail>> = flow {
        emitCachedUserDetail(username).collect { emit(it) }
        fetchUserDetail(username).collect { emit(it) }
    }

    private fun emitCachedUserDetail(username: String): Flow<BaseResponse<UserDetail>> = flow {
        val cached = localDataSource.getUserByUsername(username)
        if (cached != null) emit(BaseResponse.Success(cached.toUserDetail()))
    }

    private fun fetchUserDetail(username: String): Flow<BaseResponse<UserDetail>> {
        return safeApiCall(networkChecker, errorHandler) {
            val dto = remoteDataSource.getUserDetail(username)
            saveUserDetail(dto)
            dto.toDomain()
        }
    }

    private suspend fun saveUserDetail(dto: UserDetailDto) {
        localDataSource.upsertUsers(listOf(dto.toUserEntity()))
    }
}