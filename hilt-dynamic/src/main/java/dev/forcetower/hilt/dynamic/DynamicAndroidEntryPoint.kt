package dev.forcetower.hilt.dynamic

import dagger.hilt.GeneratesRootInput
import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@GeneratesRootInput
annotation class DynamicAndroidEntryPoint(
    /**
     * The base class for the generated Hilt class. When applying the Hilt Gradle Plugin this value
     * is not necessary and will be inferred from the current superclass.
     */
    val value: KClass<*> = Void::class
)
