package com.chigo.githubusersapp.presentation.features.userlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.chigo.githubusersapp.presentation.features.userlist.components.AppendLoadingIndicator
import com.chigo.githubusersapp.presentation.features.userlist.components.UserListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    viewModel: UserListViewModel,
    onUserClick: (String) -> Unit
) {
    val users = viewModel.users.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "GitHub Users",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // loading state — first load
            if (users.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // error state on first load
            if (users.loadState.refresh is LoadState.Error) {
                val error = (users.loadState.refresh as LoadState.Error).error
                Text(
                    text = error.message ?: "Something went wrong",
                    modifier = Modifier.align(Alignment.Center)
                )
            }


            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    count = users.itemCount,
                    key = users.itemKey { it.id }
                ) { index ->
                    val user = users[index]
                    if (user != null) {
                        UserListItem(
                            user = user,
                            onUserClick = onUserClick
                        )
                    }
                }
            }


            if (users.loadState.append is LoadState.Loading) {
                AppendLoadingIndicator(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}
