package dev.forcetower.playtime.core.model.dto.values

data class WatchProviderRegionDTO(
    val link: String,
    val rent: List<WatchProviderDTO>?,
    val buy: List<WatchProviderDTO>?,
    val flatrate: List<WatchProviderDTO>?
)
