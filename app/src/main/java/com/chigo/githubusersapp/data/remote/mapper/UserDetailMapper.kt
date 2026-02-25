package com.chigo.githubusersapp.data.remote.mapper

import com.chigo.githubusersapp.data.local.model.UserEntity
import com.chigo.githubusersapp.data.remote.model.UserDetailDto
import com.chigo.githubusersapp.domain.model.UserDetail

fun UserDetailDto.toDomain(): UserDetail {
    return UserDetail(
        id = id,
        login = login,
        avatarUrl = avatarUrl,
        bio = bio,
        followers = followers,
        publicRepos = publicRepos
    )
}

fun UserDetailDto.toUserEntity() = UserEntity(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
    type = type,
    bio = bio,
    followers = followers,
    publicRepos = publicRepos
)