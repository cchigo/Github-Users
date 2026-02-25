package com.chigo.githubusersapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.chigo.githubusersapp.data.local.datasource.UserLocalDataSource
import com.chigo.githubusersapp.data.local.db.AppDatabase
import com.chigo.githubusersapp.data.local.mapper.toUser
import com.chigo.githubusersapp.data.remote.datasource.UserRemoteDataSource
import com.chigo.githubusersapp.data.remote.paging.UserRemoteMediator
import com.chigo.githubusersapp.data.util.GeneralErrorHandler
import com.chigo.githubusersapp.domain.model.User
import com.chigo.githubusersapp.domain.repository.UserRepository
import com.chigo.githubusersapp.data.util.USERS_PER_PAGE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource,
    private val database: AppDatabase,
    private val errorHandler: GeneralErrorHandler
) : UserRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getUsers(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = USERS_PER_PAGE,
                enablePlaceholders = false
            ),
            remoteMediator = UserRemoteMediator(remoteDataSource, database),
            pagingSourceFactory = {
                localDataSource.getUsers()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toUser() }
        }
    }
}