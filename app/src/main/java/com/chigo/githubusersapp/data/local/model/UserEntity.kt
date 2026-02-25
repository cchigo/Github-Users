package com.chigo.githubusersapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chigo.githubusersapp.data.util.USERS_TABLE


@Entity(tableName = USERS_TABLE)
data class UserEntity(
    @PrimaryKey val id: Int,
    val login: String,
    val avatarUrl: String,
    val type: String,
    val bio: String?,
    val followers: Int?,
    val publicRepos: Int?
)