package com.chigo.githubusersapp.domain.model

data class UserDetail(
    val id: Int,
    val login: String,
    val avatarUrl: String,
    val bio: String?,
    val followers: Int,
    val publicRepos: Int
)