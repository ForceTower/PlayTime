package dev.forcetower.playtime.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.playtime.core.model.storage.Release
import dev.forcetower.toolkit.database.dao.BaseDao
import java.time.ZonedDateTime

@Dao
abstract class ReleaseDao : BaseDao<Release>() {
    override suspend fun getValueByIDDirect(value: Release): Release? {
        return getByIdDirect(value.movieId, value.type, value.iso, value.releaseDate)
    }

    @Query("SELECT * FROM `Release` WHERE movieId = :movieId AND type = :type AND iso = :iso AND releaseDate = :releaseDate")
    abstract suspend fun getByIdDirect(movieId: Int, type: Int, iso: String, releaseDate: ZonedDateTime): Release?

    @Query("DELETE FROM `Release` WHERE movieId = :movieId")
    abstract suspend fun deleteAllFromMovie(movieId: Int)
}