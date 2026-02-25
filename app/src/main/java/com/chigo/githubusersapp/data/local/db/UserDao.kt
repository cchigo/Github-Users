package com.chigo.githubusersapp.data.local.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.chigo.githubusersapp.data.local.model.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM users_table")
    fun getUsers(): PagingSource<Int, UserEntity>

    @Query("SELECT * FROM users_table WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserEntity?

    @Query("SELECT * FROM users_table WHERE login = :username")
    suspend fun getUserByUsername(username: String): UserEntity?
    @Upsert
    suspend fun upsertUsers(users: List<UserEntity>)

    @Query("DELETE FROM users_table")
    suspend fun clearUsers()
}