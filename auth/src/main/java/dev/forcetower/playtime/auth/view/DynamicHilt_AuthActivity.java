package dev.forcetower.playtime.auth.view;

import android.content.Context;

import androidx.activity.contextaware.OnContextAvailableListener;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.managers.ActivityComponentManager;
import dagger.hilt.internal.GeneratedComponentManagerHolder;
import dagger.hilt.internal.UnsafeCasts;
import dev.forcetower.hilt.dynamic.internal.DynamicActivityComponentDependant;
import dev.forcetower.hilt.dynamic.internal.DynamicActivityComponentManager;
import dev.forcetower.playtime.auth.injection.Hilt_AuthHiltDynamicComponent;
import dev.forcetower.toolkit.components.BaseActivity;

/**
 * A generated base class to be extended by the @dagger.hilt.android.AndroidEntryPoint annotated class. If using the Gradle plugin, this is swapped as the base class via bytecode transformation.
 */
public abstract class DynamicHilt_AuthActivity extends BaseActivity implements GeneratedComponentManagerHolder, DynamicActivityComponentDependant {
  private volatile DynamicActivityComponentManager componentManager;

  private final Object componentManagerLock = new Object();

  private boolean injected = false;

  DynamicHilt_AuthActivity() {
    super();
    _initHiltInternal();
  }

  private void _initHiltInternal() {
    addOnContextAvailableListener(new OnContextAvailableListener() {
      @Override
      public void onContextAvailable(@NotNull Context context) {
        inject();
      }
    });
  }

  @Override
  public final Object generatedComponent() {
    return this.componentManager().generatedComponent();
  }

  protected DynamicActivityComponentManager createComponentManager() {
    return new DynamicActivityComponentManager(this);
  }

  @Override
  public final DynamicActivityComponentManager componentManager() {
    if (componentManager == null) {
      synchronized (componentManagerLock) {
        if (componentManager == null) {
          componentManager = createComponentManager();
        }
      }
    }
    return componentManager;
  }

  @Override
  public final Object getDynamicComponent() {
    return Hilt_AuthHiltDynamicComponent.getComponentInstance(this);
  }

  protected void inject() {
    if (!injected) {
      injected = true;
      ((AuthActivity_GeneratedInjector) this.generatedComponent()).injectAuthActivity(UnsafeCasts.<AuthActivity>unsafeCast(this));
    }
  }

  @Override
  @NotNull
  public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
    return DefaultViewModelFactories.getActivityFactory(this, super.getDefaultViewModelProviderFactory());
  }
}
