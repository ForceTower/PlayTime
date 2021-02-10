package dev.forcetower.playtime.view.releases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.ui.ReleaseDayIndexed
import dev.forcetower.playtime.core.model.ui.ReleasesUI
import dev.forcetower.playtime.core.source.repository.MovieRepository
import dev.forcetower.playtime.view.featured.MovieActions
import dev.forcetower.toolkit.lifecycle.Event
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ReleasesViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel(), ReleasesActions {
    private val _loading = MutableLiveData(false)
    override val loading: LiveData<Boolean> = _loading
    private val _refreshing = MutableLiveData(false)
    override val refreshing: LiveData<Boolean> = _refreshing

    private val _movieClick = MutableLiveData<Event<Movie>>()
    val movieClick: LiveData<Event<Movie>> = _movieClick

    private val _scrollToEvent = MutableLiveData<Event<ReleaseScrollEvent>>()
    val scrollToEvent: LiveData<Event<ReleaseScrollEvent>> = _scrollToEvent

    private lateinit var indexer: ReleaseDayIndexed

    private var _releases: LiveData<ReleasesUI>? = null
    private var firstReleasesLoad = false

    val releases: LiveData<ReleasesUI>
        get() = releases()

    private fun releases(): LiveData<ReleasesUI> {
        val value = _releases
        if (value != null) return value
        val result = repository.releases().map {
            indexer = it.indexer
            if (!firstReleasesLoad && it.movies.isNotEmpty()) {
                firstReleasesLoad = true
                scrollToIndex(it.currentMovieReleaseIndex)
            }
            it
        }.asLiveData()
        _releases = result
        return result
    }

    private fun scrollToIndex(index: Int) {
        if (index != -1) {
            _scrollToEvent.value = Event(ReleaseScrollEvent(index))
        }
    }

    override fun onMovieClick(movie: Movie?) {
        movie ?: return
        _movieClick.value = Event(movie)
    }

    fun loadReleasesIfNeeded() {
        viewModelScope.launch {
            _loading.value = true
            _refreshing.value = true
            repository.loadCurrentReleasesIfNeeded()
            _refreshing.value = false
            _loading.value = false
        }
    }

    override fun onSwipeRefresh() {
        viewModelScope.launch {
            _refreshing.value = true
            repository.loadCurrentReleases()
            _refreshing.value = false
        }
    }

    override fun scrollToStartOfDay(date: LocalDate) {
        val index = indexer.positionForDay(date)
        _scrollToEvent.value = Event(ReleaseScrollEvent(index, true))
    }
}