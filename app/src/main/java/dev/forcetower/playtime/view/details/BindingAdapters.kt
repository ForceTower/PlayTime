package dev.forcetower.playtime.view.details

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.model.storage.Release
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@BindingAdapter("releaseDateText")
fun TextView.releaseDateText(release: Release?) {
    val now = ZonedDateTime.now()
    text = when {
        release == null -> context.getString(R.string.no_release_date_info)
        release.type < 4 -> {
            val date = release.releaseDate
            val formatted = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
            if (date.isAfter(now)) {
                context.getString(R.string.release_date_theater_future_format, formatted)
            } else {
                context.getString(R.string.release_date_theater_past_format, formatted)
            }
        }
        release.type >= 4 -> {
            val date = release.releaseDate
            val formatted = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
            if (date.isAfter(now)) {
                context.getString(R.string.release_date_digital_future_format, formatted)
            } else {
                context.getString(R.string.release_date_digital_past_format, formatted)
            }
        }
        else -> context.getString(R.string.no_release_date_info)
    }
}

@BindingAdapter(value = ["buttonReleaseAction", "onWatchlist"])
fun MaterialButton.buttonReleaseAction(release: Release?, onWatchlist: Boolean?) {
    val watchlist = onWatchlist ?: false
    val now = ZonedDateTime.now()
    when {
        release == null || release.releaseDate.isAfter(now) -> {
//            icon = ContextCompat.getDrawable(context, R.drawable.ic_outline_notifications_active_24)
            text = if (!watchlist) {
                context.getString(R.string.get_notified)
            } else {
                context.getString(R.string.remove_get_notified)
            }
        }
        release.releaseDate.isBefore(now) -> {
//            icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_add_24)
            text = if (!watchlist) {
                context.getString(R.string.add_to_watchlist)
            } else {
                context.getString(R.string.remove_from_watchlist)
            }
        }
    }
}

@BindingAdapter(value = ["watched"])
fun MaterialButton.buttonWatchedAction(watchedParam: Boolean?) {
    val watched = watchedParam ?: false
    text = if (!watched) {
        context.getString(R.string.mark_already_watched)
    } else {
        context.getString(R.string.remove_already_watched)
    }
}
