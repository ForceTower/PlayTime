package dev.forcetower.playtime.core.model.ui

import dev.forcetower.playtime.core.model.storage.Movie

data class ReleasesUI(
    val movies: List<Movie>,
    val indexer: ReleaseDayIndexed
)