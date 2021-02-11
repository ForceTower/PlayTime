package dev.forcetower.playtime.core.model.internal

import androidx.room.Embedded
import androidx.room.Relation
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.storage.Release

data class MovieWithReleases(
    @Embedded
    val movie: Movie,
    @Relation(entityColumn = "movieId", parentColumn = "id")
    val releases: List<Release>
) {
    fun earlierRelease(): Release? {
        return releases.filter { it.type >= 4 }.minByOrNull { it.releaseDate }
    }
}
