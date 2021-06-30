package dev.forcetower.playtime.core.model.storage

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(entity = Movie::class, parentColumns = ["id"], childColumns = ["movieId"], onDelete = CASCADE, onUpdate = CASCADE)
    ],
    indices = [
        Index("type"),
        Index("lang"),
        Index("movieId")
    ]
)
data class Image(
    val movieId: Int,
    val filePath: String,
    val voteAverage: Double,
    val voteCount: Int,
    val width: Int,
    val height: Int,
    val lang: String?,
    val type: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
