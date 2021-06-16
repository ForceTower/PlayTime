package dev.forcetower.playtime.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.playtime.core.model.storage.Image
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class ImageDao : BaseDao<Image>() {
    override suspend fun getValueByIDDirect(value: Image): Image? {
        return getImageByIdDirect(value.id)
    }

    @Query("SELECT * FROM Image WHERE id = :id")
    abstract suspend fun getImageByIdDirect(id: Int): Image?

    @Query("DELETE FROM Image WHERE movieId = :movieId")
    abstract suspend fun deleteAllFromMovie(movieId: Int)
}
