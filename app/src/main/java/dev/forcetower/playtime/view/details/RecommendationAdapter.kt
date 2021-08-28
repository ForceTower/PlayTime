package dev.forcetower.playtime.view.details

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.databinding.ItemMoviePosterBinding
import dev.forcetower.toolkit.extensions.inflate

class RecommendationAdapter(
    private val actions: DetailsActions
) : PagingDataAdapter<Movie, RecommendationAdapter.RecommendationHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationHolder {
        return RecommendationHolder(parent.inflate(R.layout.item_movie_poster), actions)
    }

    override fun onBindViewHolder(holder: RecommendationHolder, position: Int) {
        val item = getItem(position)
        holder.binding.movie = item
        holder.binding.root.setTag(R.id.movie_id_tag, item?.id)
    }

    inner class RecommendationHolder(val binding: ItemMoviePosterBinding, actions: DetailsActions) : RecyclerView.ViewHolder(binding.root) {
        init { binding.actions = actions }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}
