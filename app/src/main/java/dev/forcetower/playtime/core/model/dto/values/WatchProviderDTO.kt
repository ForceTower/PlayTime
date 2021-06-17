package dev.forcetower.playtime.core.model.dto.values

import dev.forcetower.playtime.core.model.storage.WatchProvider

data class WatchProviderDTO(
    val providerId: Int,
    val displayPriority: Int,
    val logoPath: String?,
    val providerName: String
) {
    fun asWatchProvider(): WatchProvider {
        return WatchProvider(
            providerId,
            providerName,
            logoPath,
            displayPriority
        )
    }
}
