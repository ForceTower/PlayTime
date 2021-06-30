package dev.forcetower.playtime.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.playtime.core.model.storage.Video
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class VideoDao : BaseDao<Video>() {
    override suspend fun getValueByIDDirect(value: Video): Video? {
        return getByIdDirect(value.id)
    }

    @Query("SELECT * FROM Video WHERE id = :id")
    abstract suspend fun getByIdDirect(id: String): Video?
}
