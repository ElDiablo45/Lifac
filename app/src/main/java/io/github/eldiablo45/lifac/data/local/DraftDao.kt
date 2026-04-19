package io.github.eldiablo45.lifac.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface DraftDao {
    @Query("SELECT * FROM invoice_drafts WHERE id = :draftId LIMIT 1")
    suspend fun getById(draftId: String): InvoiceDraftEntity?

    @Upsert
    suspend fun upsertDraft(draft: InvoiceDraftEntity)
}
