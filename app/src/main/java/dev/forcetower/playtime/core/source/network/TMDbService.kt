package dev.forcetower.playtime.core.source.network

import dev.forcetower.playtime.core.model.dto.response.GenresResponse
import dev.forcetower.playtime.core.model.dto.values.MovieDetailed
import dev.forcetower.playtime.core.model.dto.response.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDbService {
    @GET("genre/movie/list")
    suspend fun genres(): GenresResponse

    @GET("movie/popular")
    suspend fun moviesPopular(@Query("page") page: Int = 1): MoviesResponse

    @GET("movie/{movieId}")
    suspend fun movieDetails(
        @Path("movieId") movieId: Int,
        @Query("append_to_response") append: String = "videos,credits,release_dates"
    ): MovieDetailed
}