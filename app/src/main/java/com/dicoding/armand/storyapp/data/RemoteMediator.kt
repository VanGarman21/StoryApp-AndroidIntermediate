package com.dicoding.armand.storyapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dicoding.armand.storyapp.api.ApiService
import com.dicoding.armand.storyapp.data.db.RemoteKeys
import com.dicoding.armand.storyapp.data.db.StoryDatabase
import com.dicoding.armand.storyapp.data.entity.Story
import com.dicoding.armand.storyapp.utils.RemoteKeyUtils
import com.dicoding.armand.storyapp.utils.wrapEspressoIdlingResource

@OptIn(ExperimentalPagingApi::class)
class RemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService,
    private val auth: String
) : RemoteMediator<Int, Story>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Story>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> RemoteKeyUtils.getRemoteKeyClosestToCurrentPosition(state, database)?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            LoadType.PREPEND -> RemoteKeyUtils.getRemoteKeyForFirstItem(state, database)?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> RemoteKeyUtils.getRemoteKeyForLastItem(state, database)?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
        }

        wrapEspressoIdlingResource {
            try {
                val responseData = apiService.getStories(auth,page, state.config.pageSize)

                val endOfPaginationReached = responseData.body()!!.listStory.isEmpty()

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.remoteKeysDao().deleteRemoteKeys()
                        database.storyDao().deleteAll()
                    }
                    val prevKey = if (page == 1) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val keys = responseData.body()!!.listStory.map {
                        RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                    }
                    database.remoteKeysDao().insertAll(keys)
                    responseData.body()!!.listStory.forEach {
                        val story = Story(it.id, it.name, it.description, it.createdAt, it.photoUrl, it.longitude, it.latitude)
                        database.storyDao().insertStory(story)
                    }
                }
                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } catch (exception: Exception) {
                return MediatorResult.Error(exception)
            }
        }
    }

    

}
