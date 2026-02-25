package com.chigo.githubusersapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chigo.githubusersapp.data.local.model.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}