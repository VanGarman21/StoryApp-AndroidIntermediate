package com.dicoding.armand.storyapp.di

import android.content.Context
import androidx.room.Room
import com.dicoding.armand.storyapp.data.db.RemoteKeysDao
import com.dicoding.armand.storyapp.data.db.StoryDao
import com.dicoding.armand.storyapp.data.db.StoryDatabase
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
    fun provideStoryDao(storyDatabase: StoryDatabase): StoryDao = 
        storyDatabase.storyDao()

    @Provides
    fun provideRemoteKeysDao(storyDatabase: StoryDatabase): RemoteKeysDao = 
        storyDatabase.remoteKeysDao()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StoryDatabase = 
        createDatabase(context)

    private fun createDatabase(context: Context): StoryDatabase {
        return Room.databaseBuilder(context, StoryDatabase::class.java, "db_karyawan").build()
    }
}