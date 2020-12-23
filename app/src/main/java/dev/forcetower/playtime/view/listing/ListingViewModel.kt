package dev.forcetower.playtime.view.listing

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.source.repository.ListingRepository
import dev.forcetower.playtime.view.featured.MovieActions
import dev.forcetower.toolkit.lifecycle.Event
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class ListingViewModel @ViewModelInject constructor(
    private val repository: ListingRepository
) : ViewModel(), MovieActions {
    private var sourceType = 0
    private var source: Flow<PagingData<Movie>>? = null

    private val _movieClick = MutableLiveData<Event<Movie>>()
    val movieClick: LiveData<Event<Movie>> = _movieClick

    fun getListOfType(value: Int): Flow<PagingData<Movie>> {
        val current = source
        return if (value != sourceType || current == null) {
            val next = repository.getSourceOfType(value).cachedIn(viewModelScope)
            source = next
            sourceType = value
            next
        } else {
            Timber.d("return cached.")
            current
        }
    }

    override fun onMovieClick(movie: Movie?) {
        movie ?: return
        _movieClick.value = Event(movie)
    }
}