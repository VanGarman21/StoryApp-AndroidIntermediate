package com.dicoding.armand.storyapp.utils

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.armand.storyapp.data.entity.Story

class PagedTestDataSource :
    PagingSource<Int, Story>() {

    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return LoadResult.Page(
            data = emptyList(),
            prevKey = null, // Only paging forward.
            nextKey = null // Only one page available.
        )
    }

}