package dev.forcetower.playtime.view.details

import dev.forcetower.playtime.core.model.storage.Movie

interface DetailsActions {
    fun onMarkAsWatched(movie: Movie?)
    fun onAddToWatchlist(movie: Movie?)
}
