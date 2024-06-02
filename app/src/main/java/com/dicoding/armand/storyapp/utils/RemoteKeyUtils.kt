package com.dicoding.armand.storyapp.utils

import androidx.paging.PagingState
import com.dicoding.armand.storyapp.data.db.RemoteKeys
import com.dicoding.armand.storyapp.data.entity.Story
import com.dicoding.armand.storyapp.data.db.StoryDatabase

object RemoteKeyUtils {

    suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Story>, database: StoryDatabase): RemoteKeys? {
        return state.lastItemOrNull()?.let { story ->
            database.remoteKeysDao().getRemoteKeysId(story.id)
        }
    }

    suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Story>, database: StoryDatabase): RemoteKeys? {
        return state.firstItemOrNull()?.let { story ->
            database.remoteKeysDao().getRemoteKeysId(story.id)
        }
    }

    suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Story>, database: StoryDatabase): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }
}