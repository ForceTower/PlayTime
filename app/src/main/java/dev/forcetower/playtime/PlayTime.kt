package dev.forcetower.playtime

import android.app.Application
import timber.log.Timber

class PlayTime : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}