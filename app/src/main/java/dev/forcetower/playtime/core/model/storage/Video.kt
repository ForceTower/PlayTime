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
        Index("movieId")
    ]
)
data class Video(
    @PrimaryKey
    val id: String,
    val movieId: Int,
    val name: String,
    val site: String,
    val type: String,
    val key: String
)
