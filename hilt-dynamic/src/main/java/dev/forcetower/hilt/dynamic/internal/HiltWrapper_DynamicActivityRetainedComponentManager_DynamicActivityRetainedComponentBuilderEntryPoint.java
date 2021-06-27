package dev.forcetower.hilt.dynamic.internal;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;

@OriginatingElement(
    topLevelClass = DynamicActivityRetainedComponentManager.class
)
@EntryPoint
@InstallIn(SingletonComponent.class)
public interface HiltWrapper_DynamicActivityRetainedComponentManager_DynamicActivityRetainedComponentBuilderEntryPoint extends DynamicActivityRetainedComponentManager.ActivityRetainedComponentBuilderEntryPoint {
}