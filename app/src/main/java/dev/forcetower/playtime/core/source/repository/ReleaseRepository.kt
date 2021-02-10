package dev.forcetower.playtime.core.source.repository

import dev.forcetower.playtime.core.source.local.PlayDB
import dev.forcetower.playtime.core.source.network.TMDbService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReleaseRepository @Inject constructor(
    private val database: PlayDB,
    private val service: TMDbService
) {

    suspend fun releaseUpdater() {

    }
}