package dev.forcetower.playtime.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.source.repository.ListingRepository
import dev.forcetower.playtime.view.featured.MovieActions
import dev.forcetower.toolkit.lifecycle.Event
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    repository: ListingRepository
) : ViewModel(), ProfileActions {
    private val _movieClick = MutableLiveData<Event<Pair<Movie, Int>>>()
    val movieClick: LiveData<Event<Pair<Movie, Int>>> = _movieClick

    val unreleased = repository.getUserWatchlistUnreleased().cachedIn(viewModelScope)
    val released = repository.getUserWatchlistReleased().cachedIn(viewModelScope)
    val watched = repository.getWatched().cachedIn(viewModelScope)

    val countUnreleased = repository.countListUnreleased().asLiveData()
    val countReleased = repository.countListReleased().asLiveData()
    val countWatched = repository.countListWatched().asLiveData()

    override fun onMovieClick(movie: Movie?, adapter: Int?) {
        movie ?: return
        adapter ?: return
        _movieClick.value = Event(movie to adapter)
    }
}