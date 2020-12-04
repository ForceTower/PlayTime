package dev.forcetower.playtime.core.source.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import dev.forcetower.playtime.core.model.insertion.MovieBase
import dev.forcetower.playtime.core.model.insertion.MovieComplete
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.ui.MovieWithRelations
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class MovieDao : BaseDao<Movie>() {
    @Query("SELECT * FROM Movie WHERE id = :id")
    protected abstract suspend fun getByIdDirect(id: Int): Movie?

    override suspend fun getValueByIDDirect(value: Movie): Movie? {
        return getByIdDirect(value.id)
    }

    @Query("SELECT M.* FROM Movie M INNER JOIN MovieFeedIndex MFI ON M.id = MFI.movieId ORDER BY MFI.position")
    abstract fun getMovieSource(): PagingSource<Int, Movie>

    @Update(entity = Movie::class)
    abstract suspend fun updateWithBase(value: MovieBase)

    @Insert(entity = Movie::class)
    abstract suspend fun insertWithBase(value: MovieBase)

    @Transaction
    open suspend fun insertOrUpdateSimple(values: List<MovieBase>) {
        values.forEach { value ->
            val current = getByIdDirect(value.id)
            if (current != null) updateWithBase(value)
            else insertWithBase(value)
        }
    }

    @Transaction
    open suspend fun insertOrUpdateComplete(value: MovieComplete) {
        val current = getByIdDirect(value.id)
        if (current != null) updateWithComplete(value)
        else insertWithComplete(value)
    }

    @Insert(entity = Movie::class)
    abstract suspend fun insertWithComplete(value: MovieComplete)

    @Update(entity = Movie::class)
    abstract suspend fun updateWithComplete(value: MovieComplete)

    @Query("SELECT * FROM Movie WHERE id = :id")
    abstract fun getById(id: Int): LiveData<Movie>

    @Transaction
    @Query("SELECT * FROM Movie WHERE id = :id")
    abstract fun getByIdWithGenres(id: Int): LiveData<MovieWithRelations>

    @Query("DELETE FROM Movie")
    abstract suspend fun deleteAll()
}