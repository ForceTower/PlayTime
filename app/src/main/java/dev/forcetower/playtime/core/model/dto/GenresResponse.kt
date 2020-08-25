package dev.forcetower.playtime.core.model.dto

import dev.forcetower.playtime.core.model.storage.Genre

data class GenresResponse(
    val genres: List<Genre>
)