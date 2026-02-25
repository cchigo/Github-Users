package com.chigo.githubusersapp.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.chigo.githubusersapp.BuildConfig
import com.chigo.githubusersapp.data.mapper.toDomain
import com.chigo.githubusersapp.data.remote.datasource.UserRemoteDataSource
import com.chigo.githubusersapp.domain.model.User
import com.chigo.githubusersapp.data.util.STARTING_PAGE
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

class UserPagingSource(
    private val remoteDataSource: UserRemoteDataSource
) : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val since = params.key ?: STARTING_PAGE
            if (BuildConfig.DEBUG) delay(5000L) //this is to demo pagination as api can be very fast
            val users = remoteDataSource.getUsers(since)
            val domainUsers = users.map { it.toDomain() }

            LoadResult.Page(
                data = domainUsers,
                prevKey = null,
                nextKey = domainUsers.lastOrNull()?.id
            )
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}