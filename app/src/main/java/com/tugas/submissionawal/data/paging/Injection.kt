package com.tugas.submissionawal.data.paging

import android.content.Context
import com.tugas.submissionawal.api.ApiConfig
import com.tugas.submissionawal.data.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepo {
        val apiService = ApiConfig.getApiService()
        val storyDatabase = StoryDatabase.getDatabase(context)
        return StoryRepo(storyDatabase, apiService)
    }
}