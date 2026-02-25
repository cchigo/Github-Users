package com.chigo.githubusersapp.domain.usecase

import com.chigo.githubusersapp.domain.model.UserDetail
import com.chigo.githubusersapp.domain.repository.UserDetailRepository
import com.chigo.githubusersapp.data.util.BaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetUserDetailUseCase @Inject constructor(
    private val repository: UserDetailRepository
) {
    operator fun invoke(username: String): Flow<BaseResponse<UserDetail>> {
        return repository.getUserDetail(username)
    }
}
