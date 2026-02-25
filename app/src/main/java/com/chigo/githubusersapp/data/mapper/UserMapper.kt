package com.chigo.githubusersapp.data.mapper

import com.chigo.githubusersapp.data.remote.model.UserDto
import com.chigo.githubusersapp.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        login = login,
        avatarUrl = avatarUrl,
        type = type
    )
}