package dev.forcetower.playtime.view.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.storage.Release
import dev.forcetower.playtime.core.model.ui.MovieWithRelations
import dev.forcetower.playtime.core.source.repository.ListingRepository
import dev.forcetower.playtime.core.source.repository.MovieRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val listing: ListingRepository
) : ViewModel(), DetailsActions {

    fun movie(id: Int): LiveData<MovieWithRelations> {
        return repository.movie(id)
    }

    fun releaseDate(id: Int): LiveData<Release?> {
        return repository.releaseDate(id).asLiveData()
    }

    fun watched(id: Int): LiveData<Boolean> {
        return listing.watched(id).asLiveData()
    }

    fun watchlist(id: Int): LiveData<Boolean> {
        return listing.watchlist(id).asLiveData()
    }

    override fun onMarkAsWatched(movie: Movie?) {
        movie ?: return
        viewModelScope.launch { listing.toggleWatchMark(movie.id) }
    }

    override fun onAddToWatchlist(movie: Movie?) {
        movie ?: return
        viewModelScope.launch { listing.toggleFromWatchlist(movie.id) }
    }
}
