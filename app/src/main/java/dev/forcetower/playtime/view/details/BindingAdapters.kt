package dev.forcetower.playtime.view.details

import android.widget.TextView
import androidx.databinding.BindingAdapter
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