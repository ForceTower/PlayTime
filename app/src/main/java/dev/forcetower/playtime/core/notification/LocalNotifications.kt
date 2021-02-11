package dev.forcetower.playtime.core.notification

import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.bumptech.glide.Glide
import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.view.details.DetailsFragmentArgs

object LocalNotifications {
    private const val CHANNEL_ID = "track_movie_release_channel"

    fun createChannel(context: Context) {
        val name = context.getString(R.string.movie_release_notification_channel)
        val description = context.getString(R.string.movie_release_notification_channel_description)
        val channel = NotificationChannelCompat.Builder(CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_HIGH)
            .setName(name)
            .setDescription(description)
            .setShowBadge(true)
            .setVibrationEnabled(true)
            .build()

        val manager = NotificationManagerCompat.from(context)
        manager.createNotificationChannel(channel)
    }

    fun notificationForMovie(movie: Movie, count: Int, context: Context) {
        val name = movie.title

        val title = context.getString(R.string.movie_release_notification_title)
        val description = when (count) {
            0, 1 -> context.getString(R.string.movie_release_notification_description_single, name)
            else -> context.getString(R.string.movie_release_notification_description_multiple, name, count)
        }

        val intent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.home_graph)
            .setDestination(R.id.movie_details)
            .setArguments(DetailsFragmentArgs.Builder(movie.id, movie.posterPath ?: movie.backdropPath).build().toBundle())
            .createPendingIntent()

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(intent)
            .setSmallIcon(R.drawable.ic_outline_movie_24)
            .setAutoCancel(true)

        when (val image = movie.backdropPath ?: movie.posterPath) {
            null -> NotificationCompat.BigTextStyle(builder).bigText(description)
            else -> {
                val tmdbUrl = "https://image.tmdb.org/t/p/w1280${image}"
                val bitmap = Glide.with(context).asBitmap().load(tmdbUrl).submit().get()
                NotificationCompat.BigPictureStyle(builder).bigPicture(bitmap)
            }
        }

        val notification = builder.build()
        val manager = NotificationManagerCompat.from(context)
        manager.notify("release_${movie.id}", movie.id, notification)
    }
}