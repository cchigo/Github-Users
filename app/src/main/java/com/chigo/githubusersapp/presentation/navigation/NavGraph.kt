package com.chigo.githubusersapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.chigo.githubusersapp.presentation.features.userdetail.userDetailScreen
import com.chigo.githubusersapp.presentation.features.userlist.UserListDestination
import com.chigo.githubusersapp.presentation.features.userlist.userListScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = UserListDestination
    ) {
        userListScreen(
            onUserClick = { username ->
               // navController.navigateToUserDetail(username)
            }
        )
//        userDetailScreen(
//            onBackClick = {
//                navController.popBackStack()
//            }
//        )
    }
}
