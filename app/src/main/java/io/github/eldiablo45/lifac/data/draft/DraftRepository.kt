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

interface DraftRepository {
    suspend fun getActiveDraft(): StoredInvoiceDraft?

    suspend fun upsertActiveDraft(draft: StoredInvoiceDraft)
}
