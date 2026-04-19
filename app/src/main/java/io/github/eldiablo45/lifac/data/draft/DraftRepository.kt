package io.github.eldiablo45.lifac.data.draft

data class StoredInvoiceDraft(
    val id: String = ACTIVE_DRAFT_ID,
    val selectedClientId: String?,
    val selectedSeries: String,
    val nextNumberPreview: String,
    val issueDate: String,
    val operationDate: String,
    val projectLabel: String,
    val notes: String,
    val taxMode: String,
) {
    companion object {
        const val ACTIVE_DRAFT_ID = "active_draft"
    }
}

data class StoredDraftLine(
    val id: String,
    val draftId: String = StoredInvoiceDraft.ACTIVE_DRAFT_ID,
    val sortOrder: Int,
    val description: String,
    val quantity: String,
    val unitPrice: String,
    val taxMode: String,
)

data class StoredDraftBundle(
    val draft: StoredInvoiceDraft,
    val lines: List<StoredDraftLine>,
)

interface DraftRepository {
    suspend fun getActiveDraft(): StoredDraftBundle?

    suspend fun upsertActiveDraft(
        draft: StoredInvoiceDraft,
        lines: List<StoredDraftLine>,
    )
}
