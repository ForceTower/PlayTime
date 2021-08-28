package dev.forcetower.playtime.core.usecases.movie

import androidx.paging.PagingData
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.source.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecommendationsFromMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int): Flow<PagingData<Movie>> {
        return repository.recommendations(movieId)
    }
}
