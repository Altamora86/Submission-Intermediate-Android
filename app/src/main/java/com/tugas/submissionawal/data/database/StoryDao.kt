package com.tugas.submissionawal.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tugas.submissionawal.response.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<Story>)

    @Query("SELECT * FROM Story")
    fun getAllStory(): PagingSource<Int, Story>

    @Query("DELETE FROM Story")
    suspend fun deleteAll()
}