package dev.forcetower.playtime.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.playtime.core.model.storage.Image
import dev.forcetower.playtime.core.model.storage.MovieFeedIndex
import dev.forcetower.playtime.core.model.storage.MovieReleaseFeedIndex
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class ReleaseIndexDao : BaseDao<MovieReleaseFeedIndex>() {
    override suspend fun getValueByIDDirect(value: MovieReleaseFeedIndex): MovieReleaseFeedIndex? {
        return getReleaseIndexByIdDirect(value.movieId)
    }

    @Query("SELECT * FROM MovieReleaseFeedIndex WHERE movieId = :id")
    abstract suspend fun getReleaseIndexByIdDirect(id: Int): MovieReleaseFeedIndex?

    @Query("DELETE FROM MovieReleaseFeedIndex")
    abstract suspend fun deleteIndex()
}