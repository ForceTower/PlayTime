package dev.forcetower.playtime.core.model.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WatchProvider(
    @PrimaryKey
    val id: Int,
    val name: String,
    val logoPath: String?,
    val priority: Int
)
