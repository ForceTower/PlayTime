package dev.forcetower.playtime.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.forcetower.playtime.core.source.repository.ReleaseRepository
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltWorker
class ReleasesWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: ReleaseRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        try {
            Timber.d("Releases Worker started")
            repository.releaseUpdater()
            Timber.d("Releases worker completed")
        } catch (error: Throwable) {
            // This work can not die :D
            Timber.e(error, "Error on worker")
        }
        
        return Result.success()
    }

    companion object {
        private const val TAG = "main_release_worker"
        private const val NAME = "worker_release_sync"

        fun createWorker(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<ReleasesWorker>(12, TimeUnit.HOURS)
                .addTag(TAG)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(NAME, ExistingPeriodicWorkPolicy.REPLACE, request)
        }
    }
}