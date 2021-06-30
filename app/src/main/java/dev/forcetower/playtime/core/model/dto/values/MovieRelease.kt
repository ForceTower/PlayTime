package dev.forcetower.playtime.core.model.dto.values

import com.google.gson.annotations.SerializedName
import dev.forcetower.playtime.core.model.storage.Release

data class MovieRelease(
    @SerializedName("iso_3166_1")
    val iso3166_1: String,
    val releaseDates: List<MovieReleaseDate>
) {
    fun mapToReleases(movieId: Int): List<Release> {
        return releaseDates.map {
            Release(movieId, iso3166_1, it.certification, it.type, it.releaseDate)
        }
    }
}
