package com.dicoding.storyapp.app.home.paging

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dicoding.storyapp.core.data.local.paging.StoryEntity
import com.dicoding.storyapp.core.utils.dataconverter
import com.dicoding.storyapp.databinding.ItemStoryBinding

class StoryListViewHolder(
    val binding: ItemStoryBinding
): RecyclerView.ViewHolder(binding.root) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(data: StoryEntity){
        val date = data.createdAt?.let { dataconverter(it) }
        binding.photo.load(data.photoUrl)
        binding.nama.text=data.name
        binding.tanggal.text = date
        binding.deskripsi.text = data.desc
    }
}