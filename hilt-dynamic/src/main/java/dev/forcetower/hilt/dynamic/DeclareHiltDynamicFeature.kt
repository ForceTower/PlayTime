package dev.forcetower.hilt.dynamic

import kotlin.reflect.KClass

annotation class DeclareHiltDynamicFeature(
    val dependencies: Array<KClass<*>> = []
)
