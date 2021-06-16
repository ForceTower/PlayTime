package dev.forcetower.playtime.core.source.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.forcetower.playtime.core.model.insertion.MovieBase
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.source.local.PlayDB

class MovieQuerySource(
    private val queryProvider: () -> String,
    private val database: PlayDB,
    private val service: TMDbService
) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        val query = queryProvider()
        if (query.isBlank()) return LoadResult.Page(emptyList(), null, null)

        return try {
            val response = service.searchMovie(query, page)
            val movies = response.results.map { Movie.fromDTO(it) }
            val insert = response.results.map { MovieBase.fromDTO(it) }
            database.movies().insertOrUpdateSimple(insert)

            val nextPage = if (response.totalPages == page) null else page + 1
            val previousPage = if (page == 1) null else page - 1
            LoadResult.Page(movies, previousPage, nextPage)
        } catch (error: Exception) {
            LoadResult.Error(error)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return null
    }
}
