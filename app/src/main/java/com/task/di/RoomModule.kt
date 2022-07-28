package com.task.di

import android.content.Context
import com.task.db.AppDao
import com.task.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideRoomDatabaseDao(@ApplicationContext context: Context): AppDao = AppDatabase.getInstance(context = context).appDbDao()

}