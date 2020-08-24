package dev.forcetower.playtime.view.featured

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.model.ui.MovieFeatured
import dev.forcetower.playtime.databinding.ItemMovieFeaturedBinding
import dev.forcetower.toolkit.extensions.inflate

class FeaturedAdapter : ListAdapter<MovieFeatured, FeaturedAdapter.MovieHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        return MovieHolder(parent.inflate(R.layout.item_movie_featured))
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.binding.movie = getItem(position)
    }

    inner class MovieHolder(val binding: ItemMovieFeaturedBinding) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallback : DiffUtil.ItemCallback<MovieFeatured>() {
        override fun areItemsTheSame(oldItem: MovieFeatured, newItem: MovieFeatured) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MovieFeatured, newItem: MovieFeatured) = oldItem == newItem
    }
}