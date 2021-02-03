package dev.forcetower.playtime.core.model.storage

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(foreignKeys = [
    ForeignKey(entity = Movie::class, parentColumns = ["id"], childColumns = ["movieId"], onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE)
], indices = [
    Index("position")
])
data class MovieReleaseFeedIndex(
    @PrimaryKey
    val movieId: Int,
    val position: Int,
    val startReleaseDate: LocalDate
)