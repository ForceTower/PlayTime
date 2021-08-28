package dev.forcetower.playtime.view.details

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.palette.graphics.Palette
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.source.repository.ListingRepository
import dev.forcetower.playtime.core.source.repository.MovieRepository
import dev.forcetower.playtime.core.usecases.movie.GetRecommendationsFromMovieUseCase
import dev.forcetower.playtime.core.util.PaletteUtils.getFirstNonBright
import dev.forcetower.toolkit.extensions.setValueIfNew
import dev.forcetower.toolkit.lifecycle.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val listing: ListingRepository,
    private val recommendationsFromMovieUseCase: GetRecommendationsFromMovieUseCase
) : ViewModel(), DetailsActions {
    private val _movieId = MutableLiveData<Int>()

    private val _overlayColor = MutableLiveData(DEFAULT_OVERLAY_COLOR)
    override val overlayColor: LiveData<Int> = _overlayColor

    private val _onPosterLoaded = MutableLiveData<Event<Unit>>()
    val onPosterLoaded: LiveData<Event<Unit>> = _onPosterLoaded

    private val _onRecommendationsClicked = MutableLiveData<Event<Movie>>()
    val onRecommendationsClicked: LiveData<Event<Movie>> = _onRecommendationsClicked

    override val movie = _movieId.switchMap { repository.movie(it) }
    override val genres = _movieId.switchMap { repository.genres(it).asLiveData() }
    override val images = _movieId.switchMap { repository.images(it).asLiveData() }
    override val imagesAvailable = images.map { it.isNotEmpty() }
    override val release = _movieId.switchMap { repository.releaseDate(it).asLiveData() }
    override val providers = _movieId.switchMap { repository.providers(it).asLiveData() }
    override val providersAvailable = providers.map { it.isNotEmpty() }
    override val watched = _movieId.switchMap { listing.watched(it).asLiveData() }
    override val watchList = _movieId.switchMap { listing.watchlist(it).asLiveData() }
    override val video = _movieId.switchMap {
        repository
            .video(it)
            .distinctUntilChanged()
            .filterNotNull()
            .asLiveData()
    }

//    val recommendations = _movieId.asFlow()
//        .filterNotNull()
//        .flatMapLatest { recommendationsFromMovieUseCase(it) }

    fun setMovieId(id: Int) {
        _movieId.setValueIfNew(id)
    }

    fun recommendations(movieId: Int): Flow<PagingData<Movie>> {
        return recommendationsFromMovieUseCase(movieId).cachedIn(viewModelScope)
    }

    override fun onMarkAsWatched(movie: Movie?) {
        movie ?: return
        viewModelScope.launch { listing.toggleWatchMark(movie.id) }
    }

    override fun onAddToWatchlist(movie: Movie?) {
        movie ?: return
        viewModelScope.launch { listing.toggleFromWatchlist(movie.id) }
    }

    override fun onRecommendationClicked(movie: Movie?) {
        movie ?: return
        _onRecommendationsClicked.value = Event(movie)
    }

    private fun getColorFromResource(bitmap: Bitmap) {
        val palette = Palette.from(bitmap).generate()
        val dominant = palette.getFirstNonBright()
        val dominantAlpha = ColorUtils.setAlphaComponent(dominant, 0xB2)
        _overlayColor.setValueIfNew(dominantAlpha)
    }

    override val backdropListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ) = false

        override fun onResourceReady(
            resource: Drawable,
            model: Any,
            target: Target<Drawable>,
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Boolean {
            if (resource is BitmapDrawable) {
                getColorFromResource(resource.bitmap)
            }
            return false
        }
    }

    override val posterListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            _onPosterLoaded.value = Event(Unit)
            return false
        }

        override fun onResourceReady(
            resource: Drawable,
            model: Any,
            target: Target<Drawable>,
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Boolean {
            _onPosterLoaded.value = Event(Unit)
            return false
        }
    }

    companion object {
        val DEFAULT_OVERLAY_COLOR = ColorUtils.setAlphaComponent(Color.BLUE, 0x2)
    }
}
