package dev.forcetower.playtime.view.releases

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.model.ui.DayIndicator
import dev.forcetower.playtime.databinding.ItemReleaseDateIndicatorBinding
import dev.forcetower.toolkit.extensions.executeBindingsAfter
import dev.forcetower.toolkit.extensions.inflate

class DayAdapter constructor(
    private val actions: ReleasesActions,
    private val lifecycleOwner: LifecycleOwner
) : ListAdapter<DayIndicator, DayAdapter.DayHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
        return DayHolder(parent.inflate(R.layout.item_release_date_indicator))
    }

    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        holder.binding.executeBindingsAfter {
            indicator = getItem(position)
            actions = this@DayAdapter.actions
            lifecycleOwner = this@DayAdapter.lifecycleOwner
        }
    }

    inner class DayHolder(val binding: ItemReleaseDateIndicatorBinding) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallback : DiffUtil.ItemCallback<DayIndicator>() {
        override fun areItemsTheSame(oldItem: DayIndicator, newItem: DayIndicator) = oldItem == newItem
        override fun areContentsTheSame(oldItem: DayIndicator, newItem: DayIndicator) =
            oldItem.areUiContentsTheSame(newItem)
    }
}