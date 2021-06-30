package dev.forcetower.playtime.view.profile

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.databinding.ItemUserProfileListingBinding
import dev.forcetower.toolkit.extensions.executeBindingsAfter
import dev.forcetower.toolkit.extensions.inflate

class MovieAdapter constructor(
    private val actions: ProfileActions,
    private val adapter: Int
) : PagingDataAdapter<Movie, MovieAdapter.MovieHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        return MovieHolder(parent.inflate(R.layout.item_user_profile_listing), actions)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.binding.executeBindingsAfter {
            val item = getItem(position)
            movie = item
            adapter = this@MovieAdapter.adapter
            root.setTag(R.id.movie_id_tag, item?.id)
        }
    }

    inner class MovieHolder(val binding: ItemUserProfileListingBinding, actions: ProfileActions) : RecyclerView.ViewHolder(binding.root) {
        init { binding.actions = actions }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }
}
