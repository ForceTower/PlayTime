package dev.forcetower.playtime.auth.injection;

import android.content.Context;

import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.android.internal.managers.ComponentSupplier;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.internal.GeneratedComponentManager;
import dagger.hilt.internal.GeneratedComponentManagerHolder;
import dev.forcetower.hilt.dynamic.internal.DynamicFeatureComponentManager;
import dev.forcetower.playtime.auth.hilt.DaggerAuthHiltDynamicComponent_HiltComponents_DynamicC;
import dev.forcetower.playtime.injection.AuthDependencies;

public final class Hilt_AuthDynamicComponent implements GeneratedComponentManagerHolder {
    private static Hilt_AuthDynamicComponent INSTANCE = null;
    private static final Object LOCK = new Object();
    private final DynamicFeatureComponentManager componentManager;

    public Hilt_AuthDynamicComponent(Context context) {
        componentManager = new DynamicFeatureComponentManager(new ComponentSupplier() {
            @Override
            public Object get() {
                return DaggerAuthHiltDynamicComponent_HiltComponents_DynamicC.builder()
                    .applicationContextModule(new ApplicationContextModule(context.getApplicationContext()))
                    .authDependencies(
                        EntryPointAccessors.fromApplication(
                            context,
                            AuthDependencies.class
                        )
                    )
                    .build();
            }
        });
    }

    @Override
    public GeneratedComponentManager<?> componentManager() {
        return componentManager;
    }

    @Override
    public Object generatedComponent() {
        return this.componentManager().generatedComponent();
    }

    public static Object getGeneratedComponent(Context context) {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null)
                    INSTANCE = new Hilt_AuthDynamicComponent(context);
            }
        }
        return INSTANCE;
    }
}
