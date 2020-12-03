package dev.forcetower.playtime.core.model.ui

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import dev.forcetower.playtime.core.model.storage.*

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
    val videos: List<Video>,
    @Relation(entityColumn = "movieId", parentColumn = "id")
    val images: List<Image>
) {
    fun genresString(number: Int = 2) = genres.take(number).joinToString(" / ") { it.name }
}