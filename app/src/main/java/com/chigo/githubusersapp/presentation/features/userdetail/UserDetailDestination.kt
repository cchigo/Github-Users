package com.chigo.githubusersapp.presentation.features.userdetail

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class UserDetailDestination(val username: String)

fun NavGraphBuilder.userDetailScreen(
    onBackClick: () -> Unit
) {
    composable<UserDetailDestination> {
        val viewModel: UserDetailViewModel = hiltViewModel()
        UserDetailScreen(
            viewModel = viewModel,
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToUserDetail(username: String) {
    navigate(UserDetailDestination(username))
}