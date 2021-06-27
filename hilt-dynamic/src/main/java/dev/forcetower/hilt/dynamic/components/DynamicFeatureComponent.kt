package dev.forcetower.hilt.dynamic.components

import dagger.hilt.DefineComponent
import dagger.hilt.components.SingletonComponent
import dev.forcetower.hilt.dynamic.annotations.DynamicScope

@DynamicScope
@DefineComponent(parent = SingletonComponent::class)
interface DynamicFeatureComponent
