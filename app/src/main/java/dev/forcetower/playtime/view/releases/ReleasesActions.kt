package dev.forcetower.playtime.view.releases

import androidx.lifecycle.LiveData
import dev.forcetower.playtime.core.model.storage.Movie
import java.time.LocalDate

interface ReleasesActions {
    val loading: LiveData<Boolean>
    val refreshing: LiveData<Boolean>
    fun onMovieClick(movie: Movie?)
    fun onSwipeRefresh()
    fun scrollToStartOfDay(date: LocalDate)
}
