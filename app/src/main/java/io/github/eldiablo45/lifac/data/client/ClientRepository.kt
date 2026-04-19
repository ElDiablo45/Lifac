package io.github.eldiablo45.lifac.data.client

import kotlinx.coroutines.flow.Flow

data class StoredClient(
    val id: String,
    val kind: ClientType,
    val displayName: String,
    val taxId: String,
    val city: String,
    val address: String,
    val notes: String,
)

enum class ClientType {
    BUSINESS,
    INDIVIDUAL,
}

interface ClientRepository {
    fun observeClients(): Flow<List<StoredClient>>

    suspend fun upsertClient(client: StoredClient)
}
