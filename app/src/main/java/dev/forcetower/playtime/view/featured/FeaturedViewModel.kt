package dev.forcetower.playtime.view.featured

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.source.repository.MovieRepository
import dev.forcetower.toolkit.extensions.setValueIfNew
import dev.forcetower.toolkit.lifecycle.Event
import kotlinx.coroutines.flow.Flow

class FeaturedViewModel @ViewModelInject constructor(
    private val repository: MovieRepository
): ViewModel(), MovieActions {
//    private var movies: Flow<PagingData<Movie>>? = null
    private val _movieClick = MutableLiveData<Event<Movie>>()
    val movieClick: LiveData<Event<Movie>> = _movieClick

    private val _searching = MutableLiveData(false)
    val isSearching: LiveData<Boolean> = _searching

    val searchSource = repository.search { query }.cachedIn(viewModelScope)
    val movies = repository.movies().cachedIn(viewModelScope)

    var query: String = ""

//    @ExperimentalPagingApi
//    fun movies(): Flow<PagingData<Movie>> {
//        val movies = this.movies
//        if (movies != null) return movies
//        val next = repository.movies().cachedIn(viewModelScope)
//        this.movies = next
//        return next
//    }

    override fun onMovieClick(movie: Movie?) {
        movie ?: return
        _movieClick.value = Event(movie)
    }

    fun setSearching(value: Boolean) {
        _searching.setValueIfNew(value)
    }
}