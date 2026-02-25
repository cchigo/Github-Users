package com.chigo.githubusersapp.di

import com.chigo.githubusersapp.data.remote.datasource.UserRemoteDataSource
import com.chigo.githubusersapp.data.remote.datasource.UserRemoteDataSourceImpl
import com.chigo.githubusersapp.data.repository.UserRepositoryImpl
import com.chigo.githubusersapp.data.repository.UserDetailRepositoryImpl
import com.chigo.githubusersapp.domain.repository.UserRepository
import com.chigo.githubusersapp.domain.repository.UserDetailRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRemoteDataSource(
        impl: UserRemoteDataSourceImpl
    ): UserRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindUserDetailRepository(
        impl: UserDetailRepositoryImpl
    ): UserDetailRepository
}