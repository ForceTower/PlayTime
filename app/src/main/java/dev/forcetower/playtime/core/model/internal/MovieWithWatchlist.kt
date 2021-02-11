package dev.forcetower.playtime.core.model.internal

import androidx.room.Embedded
import androidx.room.Relation
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.storage.WatchlistItem

data class MovieWithWatchlist(
    @Embedded
    val movie: Movie,
    @Relation(entityColumn = "movieId", parentColumn = "id")
    val watchlistItem: WatchlistItem
)
