package io.github.eldiablo45.lifac.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert

@Dao
interface DraftDao {
    @Transaction
    @Query("SELECT * FROM invoice_drafts WHERE id = :draftId LIMIT 1")
    suspend fun getById(draftId: String): DraftWithLines?

    @Upsert
    suspend fun upsertDraft(draft: InvoiceDraftEntity)

    @Upsert
    suspend fun upsertLines(lines: List<DraftLineEntity>)

    @Query("DELETE FROM draft_lines WHERE draftId = :draftId")
    suspend fun deleteLinesByDraftId(draftId: String)
}
