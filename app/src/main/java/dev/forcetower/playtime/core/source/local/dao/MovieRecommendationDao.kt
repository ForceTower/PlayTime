package dev.forcetower.playtime.core.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.storage.MovieRecommendation
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class MovieRecommendationDao : BaseDao<MovieRecommendation>() {
    @Query("SELECT M.* FROM MovieRecommendation MR INNER JOIN Movie M ON MR.recommendedId = M.id WHERE MR.movieId = :movieId ORDER BY MR.position")
    abstract fun getRecommendationsForMovie(movieId: Int): PagingSource<Int, Movie>

    @Query("SELECT * FROM MovieRecommendation WHERE movieId = :movieId AND recommendedId = :recommendedId")
    abstract suspend fun getRecommendationByMovieAndReference(movieId: Int, recommendedId: Int): MovieRecommendation?

    override suspend fun getValueByIDDirect(value: MovieRecommendation): MovieRecommendation? {
        return getRecommendationByMovieAndReference(value.movieId, value.recommendedId)
    }

    @Query("DELETE FROM MovieRecommendation")
    abstract suspend fun deleteIndex()
}
