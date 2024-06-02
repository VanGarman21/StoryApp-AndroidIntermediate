package com.dicoding.armand.storyapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dicoding.armand.storyapp.api.ApiService
import com.dicoding.armand.storyapp.data.db.StoryDatabase
import com.dicoding.armand.storyapp.data.entity.Story
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalPagingApi::class)
class StoryRepository @Inject constructor(
    private val database: StoryDatabase,
    private val apiService: ApiService
) {

    fun getStories(authToken: String): Flow<PagingData<Story>> {
        val config = PagingConfig(pageSize = 10)
        val authHeader = createAuthHeader(authToken)
        val mediator = RemoteMediator(database, apiService, authHeader)
        val pagingSource = { database.storyDao().getStories() }

        return Pager(config = config, remoteMediator = mediator, pagingSourceFactory = pagingSource).flow
    }

    private fun createAuthHeader(token: String): String {
        return "Bearer $token"
    }

    private fun generateAuthorization(token: String): String {
        return "Bearer $token"
    }

}