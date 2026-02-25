package com.chigo.githubusersapp.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.chigo.githubusersapp.BuildConfig
import com.chigo.githubusersapp.data.remote.mapper.toDomain
import com.chigo.githubusersapp.data.remote.datasource.UserRemoteDataSource
import com.chigo.githubusersapp.data.util.GeneralErrorHandler
import com.chigo.githubusersapp.data.util.STARTING_PAGE
import com.chigo.githubusersapp.domain.model.User
import kotlinx.coroutines.delay

class UserPagingSource(
    private val remoteDataSource: UserRemoteDataSource,
    private val errorHandler: GeneralErrorHandler
) : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val since = params.key ?: STARTING_PAGE
            if (BuildConfig.DEBUG) delay(2000L) //PS: this is to demo pagination as api can be very fast
            val users = remoteDataSource.getUsers(since)
            val domainUsers = users.map { it.toDomain() }
            LoadResult.Page(
                data = domainUsers,
                prevKey = null,
                nextKey = domainUsers.lastOrNull()?.id
            )
        } catch (e: Exception) {
            val error = errorHandler.getError(e)
            LoadResult.Error(Exception(error.error.title ?: error.error.message))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}