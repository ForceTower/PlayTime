package dev.forcetower.playtime.core.model.storage

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [
    Index("position")
], foreignKeys = [
    ForeignKey(entity = Movie::class, parentColumns = ["id"], childColumns = ["movieId"], onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE)
])
data class MovieFeedIndex(
    @PrimaryKey
    val movieId: Int,
    val position: Int
)
