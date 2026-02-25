package com.chigo.githubusersapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.chigo.githubusersapp.data.remote.datasource.UserRemoteDataSource
import com.chigo.githubusersapp.data.remote.paging.UserPagingSource
import com.chigo.githubusersapp.data.util.GeneralErrorHandler
import com.chigo.githubusersapp.domain.model.User
import com.chigo.githubusersapp.domain.repository.UserRepository
import com.chigo.githubusersapp.data.util.USERS_PER_PAGE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val errorHandler: GeneralErrorHandler
) : UserRepository {

    override fun getUsers(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = USERS_PER_PAGE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                UserPagingSource(remoteDataSource, errorHandler)
            }
        ).flow
    }
}