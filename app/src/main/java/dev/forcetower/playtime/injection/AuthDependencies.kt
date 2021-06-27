package dev.forcetower.playtime.injection

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.forcetower.playtime.core.source.local.PlayDB

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AuthDependencies {
    fun database(): PlayDB
}