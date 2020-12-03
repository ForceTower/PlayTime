package dev.forcetower.playtime.view.details

import android.widget.TextView
import androidx.core.content.ContextCompat
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
            } else{
                context.getString(R.string.release_date_theater_past_format, formatted)
            }
        }
        release.type >= 4 -> {
            val date = release.releaseDate
            val formatted = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
            if (date.isAfter(now)) {
                context.getString(R.string.release_date_digital_future_format, formatted)
            } else{
                context.getString(R.string.release_date_digital_past_format, formatted)
            }
        }
        else -> context.getString(R.string.no_release_date_info)
    }
}

@BindingAdapter("buttonReleaseAction")
fun MaterialButton.buttonReleaseAction(release: Release?) {
    val now = ZonedDateTime.now()
    when {
        release == null || release.releaseDate.isAfter(now) -> {
            icon = ContextCompat.getDrawable(context, R.drawable.ic_outline_notifications_active_24)
            text = context.getString(R.string.get_notified)
        }
        release.releaseDate.isBefore(now) -> {
            icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_add_24)
            text = context.getString(R.string.add_to_watchlist)
        }
    }
}