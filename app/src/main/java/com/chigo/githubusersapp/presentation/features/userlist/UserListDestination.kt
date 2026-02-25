package com.chigo.githubusersapp.presentation.features.userlist


import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object UserListDestination

fun NavGraphBuilder.userListScreen(
    onUserClick: (String) -> Unit
) {
    composable<UserListDestination> {
        val viewModel: UserListViewModel = hiltViewModel()
        UserListScreen(
            viewModel = viewModel,
            onUserClick = onUserClick
        )
    }
}