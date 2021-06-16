package dev.forcetower.playtime.view.releases

import android.widget.TextView
import androidx.databinding.BindingAdapter
import dev.forcetower.playtime.core.model.ui.DayIndicator
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@BindingAdapter("indicatorText")
fun TextView.indicatorText(indicator: DayIndicator) {
    val res = indicator.res
    text = context.getString(res)
}

@BindingAdapter("movieReleaseDate")
fun TextView.movieReleaseDate(release: LocalDate?) {
    release ?: return
    val value = release.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
    text = value
}
