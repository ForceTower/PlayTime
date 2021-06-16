package dev.forcetower.playtime.view.featured

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.source.repository.MovieRepository
import dev.forcetower.toolkit.extensions.setValueIfNew
import dev.forcetower.toolkit.lifecycle.Event
import javax.inject.Inject

@HiltViewModel
class FeaturedViewModel @Inject constructor(
    repository: MovieRepository
) : ViewModel(), MovieActions {
    private val _movieClick = MutableLiveData<Event<Movie>>()
    val movieClick: LiveData<Event<Movie>> = _movieClick

    private val _searching = MutableLiveData(false)
    val isSearching: LiveData<Boolean> = _searching

    val searchSource = repository.search { query }.cachedIn(viewModelScope)
    val movies = repository.movies().cachedIn(viewModelScope)

    var query: String = ""

    override fun onMovieClick(movie: Movie?) {
        movie ?: return
        _movieClick.value = Event(movie)
    }

    fun setSearching(value: Boolean) {
        _searching.setValueIfNew(value)
    }
}
