package dev.forcetower.playtime.core.model.dto.response

import dev.forcetower.playtime.core.model.dto.values.WatchProviderRegionDTO

data class WatchProvidersResponse(
    val id: Int,
    val results: Map<String, WatchProviderRegionDTO>
)
