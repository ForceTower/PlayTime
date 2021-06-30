package dev.forcetower.playtime.core.model.dto.values

import com.google.gson.annotations.SerializedName
import java.time.ZonedDateTime

data class MovieReleaseDate(
    val certification: String,
    @SerializedName("iso_639_1")
    val iso639_1: String?,
    val note: String?,
    val releaseDate: ZonedDateTime,
    val type: Int
)
