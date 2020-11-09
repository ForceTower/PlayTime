package dev.forcetower.playtime.core.model.dto.response

import dev.forcetower.playtime.core.model.storage.Genre

data class GenresResponse(
    val genres: List<Genre>
)