package dev.forcetower.playtime.core.model.storage

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(foreignKeys = [
    ForeignKey(entity = Movie::class, parentColumns = ["id"], childColumns = ["movieId"], onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE)
])
data class WatchedItem(
    @PrimaryKey
    val movieId: Int,
    val addedAt: LocalDateTime = LocalDateTime.now()
)
