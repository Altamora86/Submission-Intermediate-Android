package com.tugas.submissionawal.view

import com.tugas.submissionawal.response.Story

object DataDummy {

    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                i.toString(),
                "name + $i",
                "description + $i",
                "photoUrl + $i",
                "createdAt + $i",
                "lat + $i",
                "lon $i",
            )
            items.add(story)
        }
        return items
    }
}