package com.dicoding.storyapp.app.home.paging

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.storyapp.core.data.local.paging.StoryEntity

class StroryDiffCallback: DiffUtil.ItemCallback<StoryEntity>() {
    override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
        return oldItem.id ==newItem.id
    }

    override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
        return oldItem == newItem
    }
}