package dev.forcetower.playtime.core.usecases.list

import androidx.paging.PagingData
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.source.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<PagingData<Movie>> {
        return repository.movies()
    }
}
