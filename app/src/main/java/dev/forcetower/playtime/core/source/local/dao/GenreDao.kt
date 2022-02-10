package dev.forcetower.playtime.core.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.forcetower.playtime.core.model.storage.Genre
import dev.forcetower.playtime.core.model.storage.MovieGenre
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class GenreDao : BaseDao<Genre>() {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAssociations(values: List<MovieGenre>)

    @Query("SELECT G.* FROM Genre G INNER JOIN MovieGenre MG ON MG.genreId = G.id WHERE MG.movieId = :movieId")
    abstract fun getGenres(movieId: Int): Flow<List<Genre>>
}
