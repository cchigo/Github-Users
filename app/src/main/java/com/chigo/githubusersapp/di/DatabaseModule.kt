package com.chigo.githubusersapp.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.chigo.githubusersapp.data.local.db.AppDatabase
import com.chigo.githubusersapp.data.local.db.UserDao
import com.chigo.githubusersapp.data.local.model.UserEntity
import com.chigo.githubusersapp.data.remote.datasource.UserRemoteDataSource
import com.chigo.githubusersapp.data.remote.paging.UserRemoteMediator
import com.chigo.githubusersapp.data.util.GeneralErrorHandler
import com.chigo.githubusersapp.data.util.USERS_PER_PAGE
import com.chigo.githubusersapp.data.util.USERS_TABLE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            USERS_TABLE
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideUserPager(
        database: AppDatabase,
        remoteDataSource: UserRemoteDataSource,
        errorHandler: GeneralErrorHandler
    ): Pager<Int, UserEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = USERS_PER_PAGE,
                enablePlaceholders = false
            ),
            remoteMediator = UserRemoteMediator(remoteDataSource, database, errorHandler),
            pagingSourceFactory = {
                database.userDao().getUsers()
            }
        )
    }
}