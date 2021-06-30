package dev.forcetower.playtime.view.details

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.model.storage.WatchProvider
import dev.forcetower.playtime.databinding.ItemMovieWatchProviderBinding
import dev.forcetower.toolkit.extensions.inflate

class ProviderAdapter : ListAdapter<WatchProvider, ProviderAdapter.ProviderHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProviderHolder {
        return ProviderHolder(parent.inflate(R.layout.item_movie_watch_provider))
    }

    override fun onBindViewHolder(holder: ProviderHolder, position: Int) {
        holder.binding.provider = getItem(position)
    }

    inner class ProviderHolder(val binding: ItemMovieWatchProviderBinding) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallback : DiffUtil.ItemCallback<WatchProvider>() {
        override fun areItemsTheSame(oldItem: WatchProvider, newItem: WatchProvider): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WatchProvider, newItem: WatchProvider): Boolean {
            return oldItem == newItem
        }
    }
}
