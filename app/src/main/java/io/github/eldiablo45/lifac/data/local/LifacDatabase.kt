package io.github.eldiablo45.lifac.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [ClientEntity::class, InvoiceDraftEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class LifacDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun draftDao(): DraftDao

    companion object {
        @Volatile
        private var INSTANCE: LifacDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `invoice_drafts` (
                        `id` TEXT NOT NULL,
                        `selectedClientId` TEXT,
                        `selectedSeries` TEXT NOT NULL,
                        `nextNumberPreview` TEXT NOT NULL,
                        `issueDate` TEXT NOT NULL,
                        `operationDate` TEXT NOT NULL,
                        `projectLabel` TEXT NOT NULL,
                        `notes` TEXT NOT NULL,
                        `taxMode` TEXT NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent(),
                )
            }
        }

        fun getInstance(context: Context): LifacDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    LifacDatabase::class.java,
                    "lifac.db",
                ).addMigrations(MIGRATION_1_2)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
