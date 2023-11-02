package com.dicoding.storyapp.app.home.utils

import com.dicoding.storyapp.core.data.local.paging.StoryEntity

object DataDummy {
    val name: String = "sas"

    fun generateDummyStoryResponse(): List<StoryEntity>{
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..100) {
            val story = StoryEntity(
                id = "id $i",
                name = "name $i",
                desc = "desc $i",
                photoUrl = "photoUrl $i",
                createdAt = "createdaT $i"

            )
            items.add(story)
        }
        return items

    }
}