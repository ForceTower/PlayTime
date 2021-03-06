package dev.forcetower.playtime.core.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.forcetower.playtime.core.model.storage.Genre
import dev.forcetower.playtime.core.model.storage.MovieGenre
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class GenreDao : BaseDao<Genre>() {
    @Query("SELECT * FROM Genre WHERE id = :id")
    protected abstract suspend fun getByIdDirect(id: Int): Genre?

    override suspend fun getValueByIDDirect(value: Genre): Genre? {
        return getByIdDirect(value.id)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAssociations(values: List<MovieGenre>)
}
