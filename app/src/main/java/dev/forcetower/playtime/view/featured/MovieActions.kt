package dev.forcetower.playtime.view.featured

import dev.forcetower.playtime.core.model.storage.Movie

interface MovieActions {
    fun onMovieClick(movie: Movie?)
}
