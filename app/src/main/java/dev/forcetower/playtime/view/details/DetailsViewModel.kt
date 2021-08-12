package dev.forcetower.playtime.view.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.source.repository.ListingRepository
import dev.forcetower.playtime.core.source.repository.MovieRepository
import dev.forcetower.toolkit.extensions.setValueIfNew
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val listing: ListingRepository
) : ViewModel(), DetailsActions {
    private val _movieId = MutableLiveData<Int>()

    override val movie = _movieId.switchMap { repository.movie(it) }
    override val genres = _movieId.switchMap { repository.genres(it).asLiveData() }
    override val images = _movieId.switchMap { repository.images(it).asLiveData() }
    override val release = _movieId.switchMap { repository.releaseDate(it).asLiveData() }
    override val providers = _movieId.switchMap { repository.providers(it).asLiveData() }
    override val watched = _movieId.switchMap { listing.watched(it).asLiveData() }
    override val watchList = _movieId.switchMap { listing.watchlist(it).asLiveData() }
    override val video = _movieId.switchMap {
        repository
            .video(it)
            .distinctUntilChanged()
            .filterNotNull()
            .asLiveData()
    }

    fun setMovieId(id: Int) {
        _movieId.setValueIfNew(id)
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
