package com.chigo.githubusersapp.data.repository

import com.chigo.githubusersapp.data.remote.mapper.toDomain
import com.chigo.githubusersapp.data.remote.datasource.UserRemoteDataSource
import com.chigo.githubusersapp.domain.model.UserDetail
import com.chigo.githubusersapp.domain.repository.UserDetailRepository
import com.chigo.githubusersapp.data.util.BaseResponse
import com.chigo.githubusersapp.data.util.GeneralErrorHandler
import com.chigo.githubusersapp.data.util.NetworkChecker
import com.chigo.githubusersapp.data.util.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDetailRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val networkChecker: NetworkChecker,
    private val errorHandler: GeneralErrorHandler
) : UserDetailRepository {

    override fun getUserDetail(username: String): Flow<BaseResponse<UserDetail>> {
        return safeApiCall(networkChecker, errorHandler) {
            remoteDataSource.getUserDetail(username).toDomain()
        }
    }
}