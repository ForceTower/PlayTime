package dev.forcetower.playtime.core.source.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.forcetower.playtime.core.model.insertion.MovieBase
import dev.forcetower.playtime.core.model.insertion.MovieComplete
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.ui.MovieWithRelations
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MovieDao : BaseDao<Movie>() {
    @Query("SELECT * FROM Movie WHERE id = :id")
    protected abstract suspend fun getByIdDirect(id: Int): Movie?

    @Query("SELECT M.* FROM Movie M INNER JOIN MovieFeedIndex MFI ON M.id = MFI.movieId ORDER BY MFI.position")
    abstract fun getMovieSource(): PagingSource<Int, Movie>

    @Query("SELECT M.* FROM Movie M INNER JOIN MovieReleaseFeedIndex MRFI ON M.id = MRFI.movieId ORDER BY MRFI.startReleaseDate")
    abstract fun getMovieReleaseSource(): PagingSource<Int, Movie>

    @Update(entity = Movie::class)
    abstract suspend fun updateWithBase(value: List<MovieBase>)

    @Insert(entity = Movie::class)
    abstract suspend fun insertWithBase(value: List<MovieBase>): List<Long>

    @Transaction
    open suspend fun insertOrUpdateSimple(values: List<MovieBase>) {
        val result = insertWithBase(values)
        val update = mutableListOf<MovieBase>()
        result.forEachIndexed { index, value ->
            if (value == -1L) update += values[index]
        }
        if (update.isNotEmpty()) updateWithBase(update)
    }

    @Transaction
    open suspend fun insertOrUpdateComplete(value: MovieComplete) {
        val result = insertWithComplete(value)
        if (result == -1L) {
            val current = getByIdDirect(value.id) ?: return
            if (current.releaseDate != null && value.releaseDate == null) {
                updateWithComplete(value.copy(releaseDate = current.releaseDate))
            } else {
                updateWithComplete(value)
            }
        }
    }

    @Insert(entity = Movie::class)
    abstract suspend fun insertWithComplete(value: MovieComplete): Long

    @Update(entity = Movie::class)
    abstract suspend fun updateWithComplete(value: MovieComplete)

    @Query("SELECT * FROM Movie WHERE id = :id")
    abstract fun getById(id: Int): LiveData<Movie>

    @Transaction
    @Query("SELECT * FROM Movie WHERE id = :id")
    abstract fun getByIdWithRelations(id: Int): LiveData<MovieWithRelations>

    @Query("DELETE FROM Movie")
    abstract suspend fun deleteAll()

    @Query("SELECT * FROM Movie WHERE releaseDate IS NOT NULL AND releaseDate >= :start AND releaseDate <= :end AND posterPath IS NOT NULL AND popularity > 0.3 AND overview IS NOT NULL AND LENGTH(overview) > 0 ORDER BY releaseDate")
    abstract fun getReleasesBetween(start: Long, end: Long): Flow<List<Movie>>
}
