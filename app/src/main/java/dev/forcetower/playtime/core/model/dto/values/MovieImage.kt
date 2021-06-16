package dev.forcetower.playtime.core.model.dto.values

import com.google.gson.annotations.SerializedName
import dev.forcetower.playtime.core.model.storage.Image

data class MovieImage(
    val filePath: String,
    val voteAverage: Double,
    val voteCount: Int,
    val width: Int,
    val height: Int,
    @SerializedName("iso_639_1")
    val iso639_1: String?
) {
    fun asBackdrop(movieId: Int): Image {
        return createWithType(movieId, 0)
    }

    fun asPoster(movieId: Int): Image {
        return createWithType(movieId, 1)
    }

    private fun createWithType(movieId: Int, type: Int): Image {
        return Image(
            movieId,
            filePath,
            voteAverage,
            voteCount,
            width,
            height,
            iso639_1,
            type
        )
    }
}
