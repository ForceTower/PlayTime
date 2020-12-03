package dev.forcetower.playtime.view.featured

import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.ui.MovieSimpleUI
import dev.forcetower.playtime.databinding.ItemMovieFeaturedBinding
import dev.forcetower.toolkit.extensions.inflate
import timber.log.Timber

class FeaturedAdapter(
    private val actions: MovieActions
) : PagingDataAdapter<Movie, FeaturedAdapter.MovieHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        return MovieHolder(parent.inflate(R.layout.item_movie_featured), actions)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val item = getItem(position)
        holder.binding.movie = item
        holder.binding.root.setTag(R.id.movie_id_tag, item?.id)
        holder.binding.executePendingBindings()
        ViewCompat.setTransitionName(holder.binding.cover, holder.binding.root.context.getString(R.string.transition_movie_poster, item?.id))
    }

    class MovieHolder(val binding: ItemMovieFeaturedBinding, actions: MovieActions) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.actions = actions
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }
}