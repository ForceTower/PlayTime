package dev.forcetower.playtime.view.releases

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.databinding.ItemMovieReleaseBinding
import dev.forcetower.toolkit.extensions.executeBindingsAfter
import dev.forcetower.toolkit.extensions.inflate

class ReleasesAdapter constructor(
    private val actions: ReleasesActions
) : ListAdapter<Movie, ReleasesAdapter.MovieHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReleasesAdapter.MovieHolder {
        return MovieHolder(parent.inflate(R.layout.item_movie_release), actions)
    }

    override fun onBindViewHolder(holder: ReleasesAdapter.MovieHolder, position: Int) {
        holder.binding.executeBindingsAfter {
            val item = getItem(position)
            movie = item
            root.setTag(R.id.movie_id_tag, item?.id)
        }
    }

    inner class MovieHolder(val binding: ItemMovieReleaseBinding, actions: ReleasesActions) : RecyclerView.ViewHolder(binding.root) {
        init { binding.actions = actions }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }
}