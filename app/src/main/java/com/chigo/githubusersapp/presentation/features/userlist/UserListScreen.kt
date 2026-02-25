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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.chigo.githubusersapp.R
import com.chigo.githubusersapp.presentation.features.userlist.components.AppendLoadingIndicator
import com.chigo.githubusersapp.presentation.features.userlist.components.UserListItem
import com.chigo.githubusersapp.presentation.sharedcomponents.ErrorSnackbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    viewModel: UserListViewModel,
    onUserClick: (String) -> Unit
) {
    val users = viewModel.pagingFlow.collectAsLazyPagingItems()
    val screenState by viewModel.screenState.collectAsState()

    LaunchedEffect(users.loadState) {
        viewModel.onLoadStateChanged(users.loadState, users.itemCount)
    }

    LaunchedEffect(Unit) {
        viewModel.refreshTrigger.collect {
            users.retry()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.github_users),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
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
            PullToRefreshBox(
                isRefreshing = screenState is UserListState.Refreshing,
                onRefresh = { users.refresh() },
                modifier = Modifier.fillMaxSize()
            ) {
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
            }

            // this displays our ui based on the state
            when (val state = screenState) {
                is UserListState.Idle -> {}
                is UserListState.InitialLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UserListState.Refreshing -> {}
                is UserListState.Appending -> {
                    // more pages loading, a float indicator is shown at the bottom, with a delay to show this
                    AppendLoadingIndicator(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    )
                }
                is UserListState.Error -> {
                    // error on initial load
                    ErrorSnackbar(
                        message = state.message,
                        onRetry = { users.refresh() },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    )
                }
                is UserListState.AppendError -> {
                    // failed to load next page error is shown at bottom
                    ErrorSnackbar(
                        message = state.message,
                        onRetry = { users.retry() },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    )
                }
                is UserListState.Empty -> {
                    Text(
                        text = stringResource(R.string.no_users_found),
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
               else -> {}
            }
        }
    }
}
