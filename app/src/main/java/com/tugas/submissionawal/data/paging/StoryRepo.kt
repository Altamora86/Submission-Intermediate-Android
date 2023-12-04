package com.tugas.submissionawal.data.paging

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.tugas.submissionawal.api.ApiService
import com.tugas.submissionawal.data.database.StoryDatabase
import com.tugas.submissionawal.data.mediator.StoryRemoteMediator
import com.tugas.submissionawal.response.Story

class StoryRepo (private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStory(token: String): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase,apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}