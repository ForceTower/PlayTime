package dev.forcetower.playtime.core.model.storage

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    foreignKeys = [
        ForeignKey(entity = Movie::class, parentColumns = ["id"], childColumns = ["movieId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = WatchProvider::class, parentColumns = ["id"], childColumns = ["providerId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [
        Index(value = ["movieId"]),
        Index(value = ["providerId"])
    ],
    primaryKeys = ["movieId", "providerId", "locale", "type"]
)
data class MovieWatchProvider(
    val movieId: Int,
    val providerId: Int,
    val type: Int,
    val locale: String
) {
    companion object {
        const val TYPE_FLATRATE = 0
        const val TYPE_RENT = 0
        const val TYPE_BUY = 0
    }
}
