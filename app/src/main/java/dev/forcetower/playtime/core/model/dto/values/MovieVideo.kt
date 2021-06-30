package dev.forcetower.playtime.core.model.dto.values

import dev.forcetower.playtime.core.model.storage.Video

data class MovieVideo(
    val id: String,
    val name: String,
    val site: String,
    val key: String,
    val type: String
) {
    fun asMovieVideo(movieId: Int): Video {
        return Video(id, movieId, name, site, type, key)
    }
}
