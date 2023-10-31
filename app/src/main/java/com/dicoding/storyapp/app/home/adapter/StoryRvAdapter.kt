package com.dicoding.storyapp.app.home.adapter

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dicoding.storyapp.app.details.DetailsActivity
import com.dicoding.storyapp.core.data.remote.response.ListStoryItem
import com.dicoding.storyapp.core.utils.dataconverter
import com.dicoding.storyapp.databinding.ItemStoryBinding

class StoryRvAdapter(
    private val list: List<ListStoryItem>,
): RecyclerView.Adapter<StoryRvAdapter.ViewHolder>()  {

    class ViewHolder(var binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val storyItem = list[position]

        val date = storyItem.createdAt?.let { dataconverter(it) }
        holder.binding.photo.load(storyItem.photoUrl)
        holder.binding.nama.text=storyItem.name
        holder.binding.tanggal.text =date
        holder.binding.deskripsi.text = storyItem.description

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
            intent.putExtra("id", storyItem.id)

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

    override fun getItemCount(): Int = list.size
}