package dev.forcetower.impressive

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Dependencies {
    @Provides
    @Reusable
    fun provideInfo() = DataInfo(47, "777mm Hg")
}