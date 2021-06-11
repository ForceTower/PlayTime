package dev.forcetower.core

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreDependencies {
    @Provides
    @Singleton
    fun provideBased() = BasedObject(true, "The core is here")
}