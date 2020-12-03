package dev.forcetower.playtime.core.model.storage

import androidx.room.Entity
import java.time.ZonedDateTime

/**
 * type table
 * 1 - Premiere
 * 2 - Theatrical (limited)
 * 3 - Theatrical
 * 4 - Digital
 * 5 - Physical
 * 6 - TV
 */
@Entity(primaryKeys = ["movieId", "type", "iso", "releaseDate"])
data class Release(
    val movieId: Int,
    val iso: String,
    val certification: String,
    val type: Int,
    val releaseDate: ZonedDateTime
)