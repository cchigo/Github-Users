package com.chigo.githubusersapp.presentation.features.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.chigo.githubusersapp.data.util.DEFAULT_ERROR_MESSAGE
import com.chigo.githubusersapp.data.util.NetworkChecker
import com.chigo.githubusersapp.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val networkChecker: NetworkChecker
) : ViewModel() {

    private val _users = getUsersUseCase.invoke()
        .distinctUntilChanged()
        .cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PagingData.empty()
        )
    val pagingFlow = _users

    private val _screenState = MutableStateFlow<UserListState>(UserListState.Idle)
    val screenState = _screenState.asStateFlow()

    private val _refreshTrigger = MutableSharedFlow<Unit>()
    val refreshTrigger = _refreshTrigger.asSharedFlow()

    init {
        observeNetwork()
    }

    @OptIn(FlowPreview::class)
    private fun observeNetwork() {
        viewModelScope.launch {
            networkChecker.isConnectedFlow
                .distinctUntilChanged()
                .drop(1)
                .debounce(1000L) // wait 1 second before triggering refresh
                .collect { isConnected ->
                    if (isConnected) {
                        _refreshTrigger.emit(Unit)
                    }
                }
        }
    }

    fun onLoadStateChanged(loadState: CombinedLoadStates, itemCount: Int) {
        _screenState.update {
            when {
                loadState.refresh is LoadState.Loading && itemCount == 0 ->
                    UserListState.InitialLoading
                loadState.refresh is LoadState.Loading && itemCount > 0 ->
                    UserListState.Refreshing
                loadState.append is LoadState.Loading ->
                    UserListState.Appending
                loadState.refresh is LoadState.Error -> {
                    val error = (loadState.refresh as LoadState.Error).error
                    UserListState.Error(error.message ?: DEFAULT_ERROR_MESSAGE)
                }
                loadState.append is LoadState.Error -> {
                    val error = (loadState.append as LoadState.Error).error
                    UserListState.AppendError("Unable to load more users. ${error.message}" ?: DEFAULT_ERROR_MESSAGE)
                }
                loadState.refresh is LoadState.NotLoading && itemCount == 0 ->
                    UserListState.Empty
                loadState.refresh is LoadState.NotLoading ->
                    UserListState.Success(_users.value)
                else -> UserListState.Idle
            }
        }
    }
}