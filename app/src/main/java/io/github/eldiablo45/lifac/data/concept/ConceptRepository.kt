package io.github.eldiablo45.lifac.data.concept

import kotlinx.coroutines.flow.Flow

data class StoredConcept(
    val id: String,
    val name: String,
    val description: String,
    val unitPrice: String,
    val taxMode: String,
)

interface ConceptRepository {
    fun observeConcepts(): Flow<List<StoredConcept>>

    suspend fun upsertConcept(concept: StoredConcept)
}
