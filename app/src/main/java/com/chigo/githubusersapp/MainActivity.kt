package com.chigo.githubusersapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.chigo.githubusersapp.presentation.navigation.NavGraph
import com.chigo.githubusersapp.presentation.designsystem.GithubUsersAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubUsersAppTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}