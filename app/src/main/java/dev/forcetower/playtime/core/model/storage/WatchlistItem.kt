package dev.forcetower.playtime.core.model.storage

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    foreignKeys = [
        ForeignKey(entity = Movie::class, parentColumns = ["id"], childColumns = ["movieId"], onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE)
    ],
    indices = [
        Index("targetDate"),
        Index("addedAt"),
        Index("notifiedAt"),
    ]
)
data class WatchlistItem(
    @PrimaryKey
    val movieId: Int,
    val targetDate: LocalDate?,
    val notifiedAt: LocalDateTime? = null,
    val addedAt: LocalDateTime = LocalDateTime.now()
)
