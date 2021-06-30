package dev.forcetower.playtime.core.source.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object MigrationHandlers {
    val migrations = arrayOf(
        M1TOM2
    )

    object M1TOM2 : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            val watchProvider = "WatchProvider"
            val movieWatchProvider = "MovieWatchProvider"
            database.execSQL("CREATE TABLE IF NOT EXISTS `$watchProvider` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `logoPath` TEXT, `priority` INTEGER NOT NULL, PRIMARY KEY(`id`))")
            database.execSQL("CREATE TABLE IF NOT EXISTS `$movieWatchProvider` (`movieId` INTEGER NOT NULL, `providerId` INTEGER NOT NULL, `type` INTEGER NOT NULL, `locale` TEXT NOT NULL, PRIMARY KEY(`movieId`, `providerId`, `locale`, `type`), FOREIGN KEY(`movieId`) REFERENCES `Movie`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`providerId`) REFERENCES `WatchProvider`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_MovieWatchProvider_movieId` ON `$movieWatchProvider` (`movieId`)")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_MovieWatchProvider_providerId` ON `$movieWatchProvider` (`providerId`)")
        }
    }
}
