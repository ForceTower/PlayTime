package dev.forcetower.playtime.view.featured

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.source.repository.MovieRepository
import dev.forcetower.toolkit.lifecycle.Event
import kotlinx.coroutines.flow.Flow

class FeaturedViewModel @ViewModelInject constructor(
    private val repository: MovieRepository
): ViewModel(), MovieActions {
    private val _movieClick = MutableLiveData<Event<Movie>>()
    val movieClick: LiveData<Event<Movie>> = _movieClick

    private var movies: Flow<PagingData<Movie>>? = null

    fun movies(): Flow<PagingData<Movie>> {
        val movies = this.movies
        if (movies != null) return movies
        val next = repository.movies().cachedIn(viewModelScope)
        this.movies = next
        return next
    }

    override fun onMovieClick(movie: Movie?) {
        movie ?: return
        _movieClick.value = Event(movie)
    }
}