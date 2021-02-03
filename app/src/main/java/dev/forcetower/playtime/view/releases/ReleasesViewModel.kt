package dev.forcetower.playtime.view.releases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.source.repository.MovieRepository
import dev.forcetower.playtime.view.featured.MovieActions
import dev.forcetower.toolkit.lifecycle.Event
import javax.inject.Inject

@HiltViewModel
class ReleasesViewModel @Inject constructor(
    repository: MovieRepository
) : ViewModel(), MovieActions {
    private val _movieClick = MutableLiveData<Event<Movie>>()
    val movieClick: LiveData<Event<Movie>> = _movieClick

    val releases = repository.releases().cachedIn(viewModelScope)

    override fun onMovieClick(movie: Movie?) {
        movie ?: return
        _movieClick.value = Event(movie)
    }
}