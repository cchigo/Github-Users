package com.chigo.githubusersapp.presentation.features.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.chigo.githubusersapp.data.util.DEFAULT_ERROR_MESSAGE
import com.chigo.githubusersapp.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
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

    fun onLoadStateChanged(loadState: CombinedLoadStates, itemCount: Int) {
        _screenState.value = when {
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
                UserListState.AppendError(error.message ?: DEFAULT_ERROR_MESSAGE)
            }
            loadState.refresh is LoadState.NotLoading ->
                UserListState.Success(_users.value)
            else -> UserListState.Idle
        }
    }
}