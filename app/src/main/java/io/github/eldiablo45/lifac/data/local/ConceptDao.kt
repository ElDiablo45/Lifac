package io.github.eldiablo45.lifac.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ConceptDao {
    @Query("SELECT * FROM concepts ORDER BY name COLLATE NOCASE ASC")
    fun observeConcepts(): Flow<List<ConceptEntity>>

    @Upsert
    suspend fun upsertConcept(concept: ConceptEntity)
}
