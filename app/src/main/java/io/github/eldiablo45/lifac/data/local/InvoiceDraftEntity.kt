package io.github.eldiablo45.lifac.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invoice_drafts")
data class InvoiceDraftEntity(
    @PrimaryKey val id: String,
    val selectedClientId: String?,
    val selectedSeries: String,
    val nextNumberPreview: String,
    val issueDate: String,
    val operationDate: String,
    val projectLabel: String,
    val notes: String,
    val taxMode: String,
)
