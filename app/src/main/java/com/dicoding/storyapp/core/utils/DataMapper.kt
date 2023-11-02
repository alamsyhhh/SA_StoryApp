package com.dicoding.storyapp.core.utils

import com.dicoding.storyapp.core.data.local.paging.StoryEntity
import com.dicoding.storyapp.core.data.remote.response.ListStoryItem

object DataMapper {

    fun mapResponseToEntity(input: List<ListStoryItem>): List<StoryEntity> =
        input.map {
            StoryEntity(
                id = it.id,
                name = it.name.toString(),
                desc = it.description.toString(),
                photoUrl = it.photoUrl.toString(),
                createdAt = it.createdAt.toString()
            )
        }
}