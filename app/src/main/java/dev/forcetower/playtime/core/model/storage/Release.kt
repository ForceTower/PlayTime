package dev.forcetower.playtime.core.model.storage

import androidx.room.Entity
import java.time.ZonedDateTime

@Entity(primaryKeys = ["movieId", "type", "iso", "releaseDate"])
data class Release(
    val movieId: Int,
    val iso: String,
    val certification: String,
    val type: Int,
    val releaseDate: ZonedDateTime
)