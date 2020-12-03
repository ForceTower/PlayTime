package dev.forcetower.playtime.view.details

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.model.storage.Image
import dev.forcetower.playtime.databinding.ItemMovieBackdropBinding
import dev.forcetower.toolkit.extensions.inflate

class ImagesAdapter : ListAdapter<Image, ImagesAdapter.ImageHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesAdapter.ImageHolder {
        return ImageHolder(parent.inflate(R.layout.item_movie_backdrop))
    }

    override fun onBindViewHolder(holder: ImagesAdapter.ImageHolder, position: Int) {
        holder.binding.image = getItem(position)
    }

    inner class ImageHolder(val binding: ItemMovieBackdropBinding) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallback : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.filePath == newItem.filePath
        }
    }
}