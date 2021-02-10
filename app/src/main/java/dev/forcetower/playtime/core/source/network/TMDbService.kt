package dev.forcetower.playtime.core.source.network

import dev.forcetower.playtime.core.model.dto.response.GenresResponse
import dev.forcetower.playtime.core.model.dto.values.MovieDetailed
import dev.forcetower.playtime.core.model.dto.response.MoviesResponse
import dev.forcetower.playtime.core.model.dto.response.Results
import dev.forcetower.playtime.core.model.dto.values.MovieVideo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface TMDbService {
    @GET("genre/movie/list")
    suspend fun genres(): GenresResponse

    @GET("discover/movie")
    suspend fun moviesByRelease(
        @Query("page") page: Int = 1,
        @Query("primary_release_date.gte") start: String,
        @Query("primary_release_date.lte") end: String = start,
        @Query("with_release_type") releaseType: Int = 4,
        @Query("sort_by") sorted: String = "primary_release_date.asc",
        @Query("region") region: String = "US"
    ): MoviesResponse

    @GET("movie/popular")
    suspend fun moviesPopular(@Query("page") page: Int = 1): MoviesResponse

    @GET("movie/{movieId}")
    suspend fun movieDetails(
        @Path("movieId") movieId: Int,
        @Query("append_to_response") append: String = "videos,credits,release_dates,images",
        @Query("include_image_language") imageLang: String = "${Locale.getDefault().language},null"
    ): MovieDetailed

    @GET("movie/{movieId}/videos")
    suspend fun movieVideos(
        @Path("movieId") movieId: Int,
        @Query("language") lang: String = Locale.getDefault().toLanguageTag()
    ): Results<MovieVideo>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): MoviesResponse
}