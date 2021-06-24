package dev.forcetower.playtime

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import dev.forcetower.playtime.core.log.CrashlyticsTree
import dev.forcetower.playtime.core.notification.LocalNotifications
import dev.forcetower.playtime.work.ReleasesWorker
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class PlayTime : Application(), Configuration.Provider {
    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsTree())
        }

        LocalNotifications.createChannel(this)
        ReleasesWorker.createWorker(this)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}
