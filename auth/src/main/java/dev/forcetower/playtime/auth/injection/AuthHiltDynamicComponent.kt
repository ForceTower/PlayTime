package dev.forcetower.playtime.auth.injection

import dev.forcetower.hilt.dynamic.DeclareHiltDynamicFeature
import dev.forcetower.playtime.injection.AuthDependencies

@DeclareHiltDynamicFeature(
    dependencies = [
        AuthDependencies::class
    ]
)
interface AuthHiltDynamicComponent