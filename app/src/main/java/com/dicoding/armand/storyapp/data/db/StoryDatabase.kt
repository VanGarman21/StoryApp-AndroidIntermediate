package com.dicoding.armand.storyapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dicoding.armand.storyapp.data.entity.Story

@Database(
    entities =[Story::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}