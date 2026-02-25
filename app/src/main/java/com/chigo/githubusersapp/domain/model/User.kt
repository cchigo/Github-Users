package com.chigo.githubusersapp.domain.model

data class User(
    val id: Int,
    val login: String,
    val avatarUrl: String,
    val type: String
)