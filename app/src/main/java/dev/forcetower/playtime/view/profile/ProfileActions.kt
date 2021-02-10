package dev.forcetower.playtime.view.profile

import dev.forcetower.playtime.core.model.storage.Movie

interface ProfileActions {
    fun onMovieClick(movie: Movie?, adapter: Int?)
}