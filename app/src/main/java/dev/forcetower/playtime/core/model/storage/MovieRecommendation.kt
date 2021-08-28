package dev.forcetower.playtime.core.model.storage

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["movieId", "recommendedId"],
    foreignKeys = [
        ForeignKey(entity = Movie::class, parentColumns = ["id"], childColumns = ["movieId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Movie::class, parentColumns = ["id"], childColumns = ["recommendedId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [
        Index(value = ["movieId"]),
        Index(value = ["recommendedId"])
    ]
)
data class MovieRecommendation(
    val movieId: Int,
    val recommendedId: Int,
    val position: Int
)
