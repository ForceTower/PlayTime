package dev.forcetower.playtime.core.model.ui

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import dev.forcetower.playtime.core.model.storage.Genre
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.storage.MovieGenre
import dev.forcetower.playtime.core.model.storage.Video

data class MovieWithRelations(
    @Embedded
    val movie: Movie,
    @Relation(
        entity = Genre::class,
        entityColumn = "id", parentColumn = "id",
        associateBy = Junction(value = MovieGenre::class, parentColumn = "movieId", entityColumn = "genreId")
    )
    val genres: List<Genre>,
    @Relation(entityColumn = "movieId", parentColumn = "id")
    val videos: List<Video>
) {
    fun genresString(number: Int = 2) = genres.take(number).joinToString(" / ") { it.name }
}