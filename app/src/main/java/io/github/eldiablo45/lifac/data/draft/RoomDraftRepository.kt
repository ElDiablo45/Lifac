package io.github.eldiablo45.lifac.data.draft

import android.content.Context
import io.github.eldiablo45.lifac.data.local.InvoiceDraftEntity
import io.github.eldiablo45.lifac.data.local.LifacDatabase

class RoomDraftRepository(
    private val database: LifacDatabase,
) : DraftRepository {
    override suspend fun getActiveDraft(): StoredInvoiceDraft? {
        return database.draftDao().getById(StoredInvoiceDraft.ACTIVE_DRAFT_ID)?.toStoredDraft()
    }

    override suspend fun upsertActiveDraft(draft: StoredInvoiceDraft) {
        database.draftDao().upsertDraft(
            InvoiceDraftEntity(
                id = draft.id,
                selectedClientId = draft.selectedClientId,
                selectedSeries = draft.selectedSeries,
                nextNumberPreview = draft.nextNumberPreview,
                issueDate = draft.issueDate,
                operationDate = draft.operationDate,
                projectLabel = draft.projectLabel,
                notes = draft.notes,
                taxMode = draft.taxMode,
            ),
        )
    }

    companion object {
        fun from(context: Context): RoomDraftRepository {
            return RoomDraftRepository(LifacDatabase.getInstance(context))
        }
    }
}

private fun InvoiceDraftEntity.toStoredDraft(): StoredInvoiceDraft {
    return StoredInvoiceDraft(
        id = id,
        selectedClientId = selectedClientId,
        selectedSeries = selectedSeries,
        nextNumberPreview = nextNumberPreview,
        issueDate = issueDate,
        operationDate = operationDate,
        projectLabel = projectLabel,
        notes = notes,
        taxMode = taxMode,
    )
}
