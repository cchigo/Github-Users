package com.chigo.githubusersapp.domain.usecase


import androidx.paging.PagingData
import com.chigo.githubusersapp.domain.model.User
import com.chigo.githubusersapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<PagingData<User>> {
        return repository.getUsers()
    }
}