package com.chigo.githubusersapp.presentation.features.userdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.chigo.githubusersapp.BuildConfig
import com.chigo.githubusersapp.data.util.BaseResponse
import com.chigo.githubusersapp.data.util.DEFAULT_ERROR_MESSAGE
import com.chigo.githubusersapp.domain.usecase.GetUserDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserDetailUseCase: GetUserDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val username = savedStateHandle.toRoute<UserDetailDestination>().username
    val topBarTitle: String
        get() = if (_state.value is UserDetailState.Success) {
            (_state.value as UserDetailState.Success).user.login
        } else {
            username
        }
    private val _state = MutableStateFlow<UserDetailState>(UserDetailState.Idle)
    val state = _state.asStateFlow()

    init {
        getUserDetail()
    }

    fun retry() {
        getUserDetail()
    }

    private fun getUserDetail() {
        viewModelScope.launch {
            getUserDetailUseCase(username).collect { response ->
                when (response) {
                    is BaseResponse.Loading -> {
                        _state.update { UserDetailState.Loading }
                        if (BuildConfig.DEBUG) delay(1500)
                    }
                    is BaseResponse.Success -> {
                        _state.update { UserDetailState.Success(response.data) }
                    }
                    is BaseResponse.Error -> {
                        _state.update {
                            UserDetailState.Error(
                                response.error.title ?: response.error.message ?: DEFAULT_ERROR_MESSAGE
                            )
                        }
                    }
                }
            }
        }
    }
}