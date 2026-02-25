package com.chigo.githubusersapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class UserDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("bio") val bio: String?,
    @SerializedName("followers") val followers: Int,
    @SerializedName("public_repos") val publicRepos: Int,
    @SerializedName("type") val type: String
)