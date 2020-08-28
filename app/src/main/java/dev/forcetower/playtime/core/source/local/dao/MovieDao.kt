package dev.forcetower.playtime.core.source.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import dev.forcetower.playtime.core.model.dto.MovieSimple
import dev.forcetower.playtime.core.model.insertion.MovieBase
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.ui.MovieSimpleUI
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class MovieDao : BaseDao<Movie>() {
    @Query("SELECT * FROM Movie WHERE id = :id")
    protected abstract suspend fun getByIdDirect(id: Int): Movie?

    override suspend fun getValueByIDDirect(value: Movie): Movie? {
        return getByIdDirect(value.id)
    }

    @Query("SELECT * FROM Movie ORDER BY page")
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

    @Query("SELECT * FROM Movie WHERE id = :id")
    abstract fun getById(id: Int): LiveData<Movie>

    @Query("DELETE FROM Movie")
    abstract suspend fun deleteAll()
}