package dev.forcetower.playtime.auth.injection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dev.forcetower.hilt.dynamic.components.DynamicFeatureComponent
import dev.forcetower.playtime.auth.core.AuthToken
import javax.inject.Singleton

@Module
@InstallIn(DynamicFeatureComponent::class)
object LoginModule {
    @Provides
    @Singleton
    fun provideAuthToken(): AuthToken {
        return AuthToken("1234567")
    }
}