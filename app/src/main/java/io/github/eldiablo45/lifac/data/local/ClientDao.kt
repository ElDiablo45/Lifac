package io.github.eldiablo45.lifac.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Query("SELECT * FROM clients ORDER BY displayName COLLATE NOCASE ASC")
    fun observeClients(): Flow<List<ClientEntity>>

    @Upsert
    suspend fun upsertClient(client: ClientEntity)
}
