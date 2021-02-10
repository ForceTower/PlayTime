package dev.forcetower.playtime.core.model.ui

import dev.forcetower.playtime.R
import dev.forcetower.playtime.core.model.storage.Movie
import java.time.LocalDate

class ReleaseDayIndexed(
    mapping: Map<LocalDate, Int>
) {
    // Ensure map is ordered
    init {
        var previous = -1
        mapping.forEach { (_, position) ->
            if (position <= previous) {
                throw IllegalArgumentException("Index values must be >= 0 and in ascending order")
            }
            previous = position
        }
    }

    val days = mapping.map { it.key }
    private val startPositions = mapping.map { it.value }

    fun dayForPosition(position: Int): LocalDate? {
        startPositions.asReversed().forEachIndexed { index, intVal ->
            if (intVal <= position) {
                return days[days.size - index - 1]
            }
        }
        return null
    }

    fun positionForDay(day: LocalDate): Int {
        val index = days.indexOf(day)
        if (index == -1) {
            throw IllegalArgumentException("Unknown day")
        }
        return startPositions[index]
    }

    companion object {
        private val THIS_MONTH = LocalDate.now().withDayOfMonth(1)
        private val LAST_MONTH = THIS_MONTH.minusMonths(1).withDayOfMonth(1)
        private val NEXT_MONTH = THIS_MONTH.plusMonths(2).withDayOfMonth(1).minusDays(1)

        private val ReleaseDays = listOf(LAST_MONTH, THIS_MONTH, NEXT_MONTH)

        val days = listOf(
            R.string.last_month to LAST_MONTH,
            R.string.this_month to THIS_MONTH,
            R.string.next_month to NEXT_MONTH,
        )

        fun from(movies: List<Movie>): ReleaseDayIndexed {
            val mapping = ReleaseDays
                .associateWith { day ->
                    movies.indexOfFirst { it.releaseDate?.month == day.month }
                }
                .filterValues { it >= 0 } // exclude days with no matching sessions

            return ReleaseDayIndexed(mapping)
        }
    }
}