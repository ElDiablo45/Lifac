package io.github.eldiablo45.lifac.data.draft

import android.content.Context
import androidx.room.withTransaction
import io.github.eldiablo45.lifac.data.local.DraftLineEntity
import io.github.eldiablo45.lifac.data.local.DraftWithLines
import io.github.eldiablo45.lifac.data.local.InvoiceDraftEntity
import io.github.eldiablo45.lifac.data.local.LifacDatabase

class RoomDraftRepository(
    private val database: LifacDatabase,
) : DraftRepository {
    override suspend fun getActiveDraft(): StoredDraftBundle? {
        return database.draftDao().getById(StoredInvoiceDraft.ACTIVE_DRAFT_ID)?.toStoredDraftBundle()
    }

    override suspend fun upsertActiveDraft(
        draft: StoredInvoiceDraft,
        lines: List<StoredDraftLine>,
    ) {
        database.withTransaction {
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
            database.draftDao().deleteLinesByDraftId(draft.id)
            if (lines.isNotEmpty()) {
                database.draftDao().upsertLines(
                    lines.map { line ->
                        DraftLineEntity(
                            id = line.id,
                            draftId = line.draftId,
                            sortOrder = line.sortOrder,
                            description = line.description,
                            quantity = line.quantity,
                            unitPrice = line.unitPrice,
                            taxMode = line.taxMode,
                        )
                    },
                )
            }
        }
    }

    companion object {
        fun from(context: Context): RoomDraftRepository {
            return RoomDraftRepository(LifacDatabase.getInstance(context))
        }
    }
}

private fun DraftWithLines.toStoredDraftBundle(): StoredDraftBundle {
    return StoredDraftBundle(
        draft = StoredInvoiceDraft(
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
        lines = lines
            .sortedBy { it.sortOrder }
            .map { line ->
                StoredDraftLine(
                    id = line.id,
                    draftId = line.draftId,
                    sortOrder = line.sortOrder,
                    description = line.description,
                    quantity = line.quantity,
                    unitPrice = line.unitPrice,
                    taxMode = line.taxMode,
                )
            },
    )
}
