package com.chigo.githubusersapp.data.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.chigo.githubusersapp.data.local.db.AppDatabase
import com.chigo.githubusersapp.data.local.model.UserEntity
import com.chigo.githubusersapp.data.remote.datasource.UserRemoteDataSource
import com.chigo.githubusersapp.data.remote.mapper.toUserEntity
import com.chigo.githubusersapp.data.util.GeneralErrorHandler
import com.chigo.githubusersapp.data.util.STARTING_PAGE
/**
 * We assume GitHub's `since` parameter efficiently represents the last fetched key,
 *
 */
@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val remoteDataSource: UserRemoteDataSource,
    private val database: AppDatabase,
    private val errorHandler: GeneralErrorHandler
) : RemoteMediator<Int, UserEntity>() {

    private val userDao = database.userDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        return try {
            val since = when (loadType) {
                LoadType.REFRESH -> STARTING_PAGE
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    state.lastItemOrNull()?.id
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val users = remoteDataSource.getUsers(since)

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    userDao.clearUsers()
                }
                userDao.upsertUsers(users.map { it.toUserEntity() })
            }

            MediatorResult.Success(endOfPaginationReached = users.isEmpty())
        } catch (e: Exception) {
            val error = errorHandler.getError(e)
            MediatorResult.Error(Exception(error.error.title ?: error.error.message))
        }
    }
}