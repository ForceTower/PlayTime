package dev.forcetower.playtime.auth.hilt;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Component;
import dagger.Module;
import dagger.Subcomponent;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.components.ViewComponent;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.components.ViewWithFragmentComponent;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_DefaultViewModelFactories_ActivityModule;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_HiltViewModelFactory_ActivityCreatorEntryPoint;
import dagger.hilt.android.internal.managers.ActivityComponentManager;
import dagger.hilt.android.internal.managers.FragmentComponentManager;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedComponentBuilderEntryPoint;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedLifecycleEntryPoint;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_LifecycleModule;
import dagger.hilt.android.internal.managers.ViewComponentManager;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.HiltWrapper_ActivityModule;
import dagger.hilt.android.scopes.ActivityRetainedScoped;
import dagger.hilt.android.scopes.ActivityScoped;
import dagger.hilt.android.scopes.FragmentScoped;
import dagger.hilt.android.scopes.ViewModelScoped;
import dagger.hilt.android.scopes.ViewScoped;
import dagger.hilt.internal.GeneratedComponent;
import dagger.hilt.migration.DisableInstallInCheck;
import dev.forcetower.hilt.dynamic.components.DynamicFeatureComponent;
import dev.forcetower.hilt.dynamic.internal.DynamicActivityComponentManager;
import dev.forcetower.hilt.dynamic.internal.HiltWrapper_DynamicActivityRetainedComponentManager_DynamicActivityRetainedComponentBuilderEntryPoint;
import dev.forcetower.playtime.auth.injection.LoginModule;
import dev.forcetower.playtime.auth.view.AuthActivity_GeneratedInjector;
import dev.forcetower.playtime.auth.view.login.LoginFragment_GeneratedInjector;
import dev.forcetower.playtime.auth.view.login.LoginViewModel_HiltModules;
import dev.forcetower.playtime.injection.AuthDependencies;
import dev.forcetower.playtime.view.details.DetailsViewModel_HiltModules;
import dev.forcetower.playtime.view.featured.FeaturedViewModel_HiltModules;

public final class AuthHiltDynamicComponent_HiltComponents {
    private AuthHiltDynamicComponent_HiltComponents() {}

    // Activity
    @Module(
        subcomponents = {
            AuthHiltDynamicComponent_HiltComponents.ActivityRetainedC.class
        }
    )
    @DisableInstallInCheck
    interface ActivityRetainedCBuilderModule {
        @Binds
        ActivityRetainedComponentBuilder bind(AuthHiltDynamicComponent_HiltComponents.ActivityRetainedC.Builder builder);
    }

    @Module(
        subcomponents = AuthHiltDynamicComponent_HiltComponents.ActivityC.class
    )
    @DisableInstallInCheck
    interface ActivityCBuilderModule {
        @Binds
        ActivityComponentBuilder bind(AuthHiltDynamicComponent_HiltComponents.ActivityC.Builder builder);
    }

    // ViewModel
    @Module(
        subcomponents = AuthHiltDynamicComponent_HiltComponents.ViewModelC.class
    )
    @DisableInstallInCheck
    interface ViewModelCBuilderModule {
        @Binds
        ViewModelComponentBuilder bind(AuthHiltDynamicComponent_HiltComponents.ViewModelC.Builder builder);
    }

    @Module(
        subcomponents = AuthHiltDynamicComponent_HiltComponents.FragmentC.class
    )
    @DisableInstallInCheck
    interface FragmentCBuilderModule {
        @Binds
        FragmentComponentBuilder bind(AuthHiltDynamicComponent_HiltComponents.FragmentC.Builder builder);
    }


    @Module(
        subcomponents = AuthHiltDynamicComponent_HiltComponents.ViewC.class
    )
    @DisableInstallInCheck
    interface ViewCBuilderModule {
        @Binds
        ViewComponentBuilder bind(AuthHiltDynamicComponent_HiltComponents.ViewC.Builder builder);
    }

    @Module(
            subcomponents = AuthHiltDynamicComponent_HiltComponents.ViewWithFragmentC.class
    )
    @DisableInstallInCheck
    interface ViewWithFragmentCBuilderModule {
        @Binds
        ViewWithFragmentComponentBuilder bind(AuthHiltDynamicComponent_HiltComponents.ViewWithFragmentC.Builder builder);
    }

    @Component(
        dependencies = {
            AuthDependencies.class
        },
        modules = {
            ApplicationContextModule.class,
            LoginModule.class,
            ActivityRetainedCBuilderModule.class
        }
    )
    @Singleton
    public abstract static class DynamicC implements HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedComponentBuilderEntryPoint,
            HiltWrapper_DynamicActivityRetainedComponentManager_DynamicActivityRetainedComponentBuilderEntryPoint,
            DynamicFeatureComponent,
            GeneratedComponent {

    }

    @Subcomponent(
        modules = {
            DetailsViewModel_HiltModules.KeyModule.class,
            FeaturedViewModel_HiltModules.KeyModule.class,
            HiltWrapper_ActivityRetainedComponentManager_LifecycleModule.class,
            AuthHiltDynamicComponent_HiltComponents.ActivityCBuilderModule.class,
            AuthHiltDynamicComponent_HiltComponents.ViewModelCBuilderModule.class,
            LoginViewModel_HiltModules.KeyModule.class,
        }
    )
    @ActivityRetainedScoped
    public abstract static class ActivityRetainedC implements ActivityRetainedComponent,
            ActivityComponentManager.ActivityComponentBuilderEntryPoint,
            DynamicActivityComponentManager.ActivityComponentBuilderEntryPoint,
            HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedLifecycleEntryPoint,
            HiltWrapper_DynamicActivityRetainedComponentManager_DynamicActivityRetainedComponentBuilderEntryPoint,
            GeneratedComponent {
        @Subcomponent.Builder
        interface Builder extends ActivityRetainedComponentBuilder {
        }
    }

    @Subcomponent(
        modules = {
            HiltWrapper_ActivityModule.class,
            HiltWrapper_DefaultViewModelFactories_ActivityModule.class,
            AuthHiltDynamicComponent_HiltComponents.FragmentCBuilderModule.class,
            AuthHiltDynamicComponent_HiltComponents.ViewCBuilderModule.class
        }
    )
    @ActivityScoped
    public abstract static class ActivityC implements ActivityComponent,
            DefaultViewModelFactories.ActivityEntryPoint,
            HiltWrapper_HiltViewModelFactory_ActivityCreatorEntryPoint,
            FragmentComponentManager.FragmentComponentBuilderEntryPoint,
            ViewComponentManager.ViewComponentBuilderEntryPoint,
            GeneratedComponent,
            AuthActivity_GeneratedInjector {
        @Subcomponent.Builder
        interface Builder extends ActivityComponentBuilder {
        }
    }


    @Subcomponent(
        modules = {
            LoginViewModel_HiltModules.BindsModule.class
        }
    )
    @ViewModelScoped
    public abstract static class ViewModelC implements ViewModelComponent,
            HiltViewModelFactory.ViewModelFactoriesEntryPoint,
            GeneratedComponent {
        @Subcomponent.Builder
        interface Builder extends ViewModelComponentBuilder {
        }
    }

    @Subcomponent(
        modules = AuthHiltDynamicComponent_HiltComponents.ViewWithFragmentCBuilderModule.class
    )
    @FragmentScoped
    public abstract static class FragmentC implements FragmentComponent,
            DefaultViewModelFactories.FragmentEntryPoint,
            ViewComponentManager.ViewWithFragmentComponentBuilderEntryPoint,
            GeneratedComponent,
            LoginFragment_GeneratedInjector {
        @Subcomponent.Builder
        interface Builder extends FragmentComponentBuilder {
        }
    }

    @Subcomponent
    @ViewScoped
    public abstract static class ViewC implements ViewComponent,
            GeneratedComponent {
        @Subcomponent.Builder
        interface Builder extends ViewComponentBuilder {
        }
    }

    @Subcomponent
    @ViewScoped
    public abstract static class ViewWithFragmentC implements ViewWithFragmentComponent,
            GeneratedComponent {
        @Subcomponent.Builder
        interface Builder extends ViewWithFragmentComponentBuilder {
        }
    }
}
