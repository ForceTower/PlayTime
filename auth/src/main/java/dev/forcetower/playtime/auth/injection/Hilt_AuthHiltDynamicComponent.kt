package dev.forcetower.playtime.auth.injection

import android.content.Context
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.internal.modules.ApplicationContextModule
import dagger.hilt.internal.GeneratedComponentManager
import dagger.hilt.internal.GeneratedComponentManagerHolder
import dev.forcetower.hilt.dynamic.internal.DynamicFeatureComponentManager
import dev.forcetower.playtime.auth.hilt.DaggerAuthHiltDynamicComponent_HiltComponents_DynamicC
import dev.forcetower.playtime.injection.AuthDependencies

@Suppress("ClassName")
class Hilt_AuthHiltDynamicComponent(context: Context) : AuthHiltDynamicComponent, GeneratedComponentManagerHolder {
    private val component =
        DynamicFeatureComponentManager {
            DaggerAuthHiltDynamicComponent_HiltComponents_DynamicC.builder()
                .applicationContextModule(
                    ApplicationContextModule(
                        context.applicationContext
                    )
                )
                .authDependencies(
                    EntryPointAccessors.fromApplication(
                        context,
                        AuthDependencies::class.java
                    )
                )
                .build()
        }

    override fun generatedComponent(): Any {
        return this.componentManager().generatedComponent()
    }

    override fun componentManager(): GeneratedComponentManager<*> {
        return component
    }

    companion object {
        private val LOCK = Any()
        private var instance: Hilt_AuthHiltDynamicComponent? = null

        @JvmStatic
        fun getComponentInstance(context: Context): Hilt_AuthHiltDynamicComponent {
            if (instance == null) {
                synchronized(LOCK) {
                    if (instance == null) {
                        instance = Hilt_AuthHiltDynamicComponent(context)
                    }
                }
            }
            return instance!!
        }
    }
}