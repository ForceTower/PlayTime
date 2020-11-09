package dev.forcetower.playtime.core.model.storage

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index

@Entity(foreignKeys = [
    ForeignKey(entity = Movie::class, parentColumns = ["id"], childColumns = ["movieId"], onDelete = CASCADE),
    ForeignKey(entity = Genre::class, parentColumns = ["id"], childColumns = ["genreId"], onDelete = CASCADE)
], indices = [
    Index(value = ["movieId"]),
    Index(value = ["genreId"])
], primaryKeys = ["movieId", "genreId"])
data class MovieGenre(
    val movieId: Int,
    val genreId: Int
)