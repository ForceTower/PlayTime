package dev.forcetower.playtime.core.model.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Movie(
    @PrimaryKey
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val runtime: Int?,
    val video: Boolean,
    val adult: Boolean,
    val voteAverage: Double,
    val releaseDate: LocalDate?,
    val tagline: String?,
    val status: String?,
    @ColumnInfo(defaultValue = "0")
    val position: Int
)