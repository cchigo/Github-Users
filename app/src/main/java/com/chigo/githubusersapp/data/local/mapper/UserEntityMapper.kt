
package com.chigo.githubusersapp.data.local.mapper

import com.chigo.githubusersapp.data.local.model.UserEntity
import com.chigo.githubusersapp.domain.model.User
import com.chigo.githubusersapp.domain.model.UserDetail

fun UserEntity.toUser() = User(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
    type = type
)

fun UserEntity.toUserDetail() = UserDetail(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
    bio = bio,
    followers = followers ?: 0,
    publicRepos = publicRepos ?: 0
)