package dev.forcetower.playtime.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.playtime.core.model.storage.Release
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

@Dao
abstract class ReleaseDao : BaseDao<Release>() {
    @Query("SELECT * FROM `Release` WHERE movieId = :movieId AND type = :type AND iso = :iso AND releaseDate = :releaseDate")
    abstract suspend fun getByIdDirect(movieId: Int, type: Int, iso: String, releaseDate: ZonedDateTime): Release?

    @Query("DELETE FROM `Release` WHERE movieId = :movieId")
    abstract suspend fun deleteAllFromMovie(movieId: Int)

    @Query("SELECT * FROM `Release` WHERE movieId = :movieId")
    abstract fun getReleaseDates(movieId: Int): Flow<List<Release>>
}
