package io.github.eldiablo45.lifac.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ClientEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class LifacDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao

    companion object {
        @Volatile
        private var INSTANCE: LifacDatabase? = null

        fun getInstance(context: Context): LifacDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    LifacDatabase::class.java,
                    "lifac.db",
                ).build().also { INSTANCE = it }
            }
        }
    }
}
