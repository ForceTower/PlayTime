package dev.forcetower.playtime.core.model.storage

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(entity = Movie::class, parentColumns = ["id"], childColumns = ["movieId"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
], indices = [
    Index("movieId")
])
data class Cast(
    @PrimaryKey
    val creditId: String,
    val movieId: Int,
    val castId: Int,
    val character: String,
    val id: Int,
    val name: String,
    val profilePath: String?,
    val order: Int
)