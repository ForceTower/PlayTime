package dev.forcetower.playtime.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.playtime.core.model.storage.Video
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class VideoDao : BaseDao<Video>() {
    @Query("SELECT * FROM Video WHERE id = :id")
    abstract suspend fun getByIdDirect(id: String): Video?

    @Query("SELECT * FROM Video WHERE movieId = :movieId AND site = 'YouTube' LIMIT 1")
    abstract fun getFirstYouTubeVideo(movieId: Int): Flow<Video?>
}
