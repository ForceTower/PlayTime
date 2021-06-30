package dev.forcetower.playtime.core.model.ui

import androidx.annotation.StringRes
import java.time.LocalDate

data class DayIndicator(
    @StringRes
    val res: Int,
    val date: LocalDate,
    val checked: Boolean
) {
    override fun equals(other: Any?): Boolean =
        this === other || (other is DayIndicator && date == other.date)

    override fun hashCode(): Int = date.hashCode()

    fun areUiContentsTheSame(other: DayIndicator) = checked == other.checked
}
