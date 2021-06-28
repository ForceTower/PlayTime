package dev.forcetower.hilt.dynamic

import dagger.hilt.GeneratesRootInput
import java.lang.annotation.ElementType
import kotlin.reflect.KClass

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@GeneratesRootInput
annotation class DeclareHiltDynamicFeature(
    val dependencies: Array<KClass<*>> = []
)
