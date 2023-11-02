package com.dicoding.storyapp.app.home.paging

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.dicoding.storyapp.app.details.DetailsActivity
import com.dicoding.storyapp.core.data.local.paging.StoryEntity
import com.dicoding.storyapp.databinding.ItemStoryBinding

class StoryPagingAdapter: PagingDataAdapter<StoryEntity, StoryListViewHolder>(DIFF_CALLBACK) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: StoryListViewHolder, position: Int) {
        val data = getItem(position)

        if (data != null) {
            holder.bind(data)
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
                intent.putExtra("id", data.id)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        holder.itemView.context as Activity,
                        androidx.core.util.Pair(holder.binding.photo, "photo"),
                        androidx.core.util.Pair(holder.binding.nama, "name"),
                        androidx.core.util.Pair( holder.binding.deskripsi, "desc"),
                        androidx.core.util.Pair(holder.binding.tanggal, "date")
                    )
                holder.itemView.context.startActivity(intent, optionsCompat.toBundle())

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryListViewHolder {
        return StoryListViewHolder(
            ItemStoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}