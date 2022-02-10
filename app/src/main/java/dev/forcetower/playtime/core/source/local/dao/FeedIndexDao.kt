package dev.forcetower.playtime.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.playtime.core.model.storage.MovieFeedIndex
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class FeedIndexDao : BaseDao<MovieFeedIndex>() {
    @Query("SELECT * FROM MovieFeedIndex WHERE movieId = :id")
    abstract suspend fun getFeedIndexByIdDirect(id: Int): MovieFeedIndex?

    @Query("DELETE FROM MovieFeedIndex")
    abstract suspend fun deleteIndex()
}
