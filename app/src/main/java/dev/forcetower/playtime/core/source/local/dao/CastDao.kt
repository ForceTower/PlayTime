package dev.forcetower.playtime.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.playtime.core.model.storage.Cast
import dev.forcetower.playtime.core.model.storage.Video
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CastDao : BaseDao<Cast>() {
    override suspend fun getValueByIDDirect(value: Cast): Cast? {
        return getByIdDirect(value.creditId)
    }

    @Query("SELECT * FROM `Cast` WHERE creditId = :id")
    abstract suspend fun getByIdDirect(id: String): Cast?

    @Query("SELECT * FROM `Cast` WHERE movieId = :movieId")
    abstract fun getCastFromMovie(movieId: Int): Flow<List<Cast>>
}