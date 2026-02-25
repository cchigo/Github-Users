package com.chigo.githubusersapp.data.local.datasource

import androidx.paging.PagingSource
import com.chigo.githubusersapp.data.local.db.UserDao
import com.chigo.githubusersapp.data.local.model.UserEntity
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao
) : UserLocalDataSource {

    override fun getUsers(): PagingSource<Int, UserEntity> {
        return userDao.getUsers()
    }

    override suspend fun getUserById(userId: Int): UserEntity? {
        return userDao.getUserById(userId)
    }
    override suspend fun getUserByUsername(username: String): UserEntity? = userDao.getUserByUsername(username)

    override suspend fun clearUsers() = userDao.clearUsers()

    override suspend fun upsertUsers(users: List<UserEntity>) {
        userDao.upsertUsers(users)
    }
}