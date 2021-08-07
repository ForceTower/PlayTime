package dev.forcetower.playtime.view.featured

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.usecases.list.GetPopularMoviesUseCase
import dev.forcetower.playtime.core.usecases.list.SearchMoviesUseCase
import dev.forcetower.toolkit.extensions.setValueIfNew
import dev.forcetower.toolkit.lifecycle.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeaturedViewModel @Inject constructor(
    getPopularMovies: GetPopularMoviesUseCase,
    searchMovies: SearchMoviesUseCase
) : ViewModel(), MovieActions {
    private val _movieClick = MutableLiveData<Event<Movie>>()
    val movieClick: LiveData<Event<Movie>> = _movieClick

    private val _searching = MutableLiveData(false)
    val isSearching: LiveData<Boolean> = _searching

    private val _onRefreshSearch = MutableLiveData<Event<Unit>>()
    val onRefreshSearch: LiveData<Event<Unit>> = _onRefreshSearch

    val searchSource = searchMovies { query }.cachedIn(viewModelScope)
    val movies = getPopularMovies().cachedIn(viewModelScope)

    private var searchJob: Job? = null
    private var query: String = ""

    override fun onMovieClick(movie: Movie?) {
        movie ?: return
        _movieClick.value = Event(movie)
    }

    fun setSearching(value: Boolean) {
        _searching.setValueIfNew(value)
    }

    fun onSearchQueryChanged(newQuery: String) {
        if (query != newQuery) {
            query = newQuery
            executeSearch()
        }
    }

    private fun executeSearch() {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(250L)
            _onRefreshSearch.value = Event(Unit)
        }
    }
}
