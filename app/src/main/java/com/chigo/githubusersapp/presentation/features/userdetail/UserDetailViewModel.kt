package com.chigo.githubusersapp.presentation.features.userdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.chigo.githubusersapp.domain.usecase.GetUserDetailUseCase
import com.chigo.githubusersapp.data.util.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _state = MutableStateFlow(UserDetailState())
    val state = _state.asStateFlow()

    init {
        getUserDetail()
    }

    private fun getUserDetail() {
        viewModelScope.launch {
            getUserDetailUseCase(username).collect { response ->
                when (response) {
                    is BaseResponse.Loading -> _state.update {
                        it.copy(isLoading = true, error = null)
                    }
                    is BaseResponse.Success -> _state.update {
                        it.copy(
                            isLoading = false,
                            userDetail = response.data,
                            error = null
                        )
                    }
                    is BaseResponse.Error -> _state.update {
                        it.copy(
                            isLoading = false,
                            error = response.error.title ?: response.error.message
                        )
                    }
                }
            }
        }
    }
}