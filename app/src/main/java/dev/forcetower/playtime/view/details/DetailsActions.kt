package dev.forcetower.playtime.view.details

import androidx.lifecycle.LiveData
import dev.forcetower.playtime.core.model.storage.Genre
import dev.forcetower.playtime.core.model.storage.Image
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.storage.Release
import dev.forcetower.playtime.core.model.storage.Video
import dev.forcetower.playtime.core.model.storage.WatchProvider

interface DetailsActions {
    val movie: LiveData<Movie>
    val genres: LiveData<List<Genre>>
    val images: LiveData<List<Image>>
    val video: LiveData<Video>
    val release: LiveData<Release?>
    val providers: LiveData<List<WatchProvider>>
    val watched: LiveData<Boolean>
    val watchList: LiveData<Boolean>
    fun onMarkAsWatched(movie: Movie?)
    fun onAddToWatchlist(movie: Movie?)
}
