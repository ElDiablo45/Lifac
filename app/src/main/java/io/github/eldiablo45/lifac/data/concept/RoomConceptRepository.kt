package io.github.eldiablo45.lifac.data.concept

import android.content.Context
import io.github.eldiablo45.lifac.data.local.ConceptEntity
import io.github.eldiablo45.lifac.data.local.LifacDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomConceptRepository(
    private val database: LifacDatabase,
) : ConceptRepository {
    override fun observeConcepts(): Flow<List<StoredConcept>> {
        return database.conceptDao().observeConcepts().map { entities ->
            entities.map { entity ->
                StoredConcept(
                    id = entity.id,
                    name = entity.name,
                    description = entity.description,
                    unitPrice = entity.unitPrice,
                    taxMode = entity.taxMode,
                )
            }
        }
    }

    override suspend fun upsertConcept(concept: StoredConcept) {
        database.conceptDao().upsertConcept(
            ConceptEntity(
                id = concept.id,
                name = concept.name,
                description = concept.description,
                unitPrice = concept.unitPrice,
                taxMode = concept.taxMode,
            ),
        )
    }

    companion object {
        fun from(context: Context): RoomConceptRepository {
            return RoomConceptRepository(LifacDatabase.getInstance(context))
        }
    }
}
