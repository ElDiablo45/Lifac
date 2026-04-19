package io.github.eldiablo45.lifac.data.client

import android.content.Context
import io.github.eldiablo45.lifac.data.local.ClientDao
import io.github.eldiablo45.lifac.data.local.LifacDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomClientRepository(
    private val clientDao: ClientDao,
) : ClientRepository {
    override fun observeClients(): Flow<List<StoredClient>> {
        return clientDao.observeClients().map { entities ->
            entities.map { entity ->
                StoredClient(
                    id = entity.id,
                    kind = ClientType.valueOf(entity.kind),
                    displayName = entity.displayName,
                    taxId = entity.taxId,
                    city = entity.city,
                    address = entity.address,
                    notes = entity.notes,
                )
            }
        }
    }

    override suspend fun upsertClient(client: StoredClient) {
        clientDao.upsertClient(
            io.github.eldiablo45.lifac.data.local.ClientEntity(
                id = client.id,
                kind = client.kind.name,
                displayName = client.displayName,
                taxId = client.taxId,
                city = client.city,
                address = client.address,
                notes = client.notes,
            ),
        )
    }

    companion object {
        fun from(context: Context): RoomClientRepository {
            return RoomClientRepository(LifacDatabase.getInstance(context).clientDao())
        }
    }
}
