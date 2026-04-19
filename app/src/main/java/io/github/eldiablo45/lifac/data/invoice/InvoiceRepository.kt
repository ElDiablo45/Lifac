package io.github.eldiablo45.lifac.data.invoice

import kotlinx.coroutines.flow.Flow

data class StoredInvoiceSummary(
    val id: String,
    val number: String,
    val clientName: String,
    val status: String,
    val total: String,
    val issueDate: String,
    val createdAt: Long,
)

data class StoredInvoice(
    val id: String,
    val clientId: String,
    val status: String,
    val series: String,
    val number: String,
    val issueDate: String,
    val operationDate: String,
    val projectLabel: String,
    val notes: String,
    val taxMode: String,
    val subtotal: String,
    val taxTotal: String,
    val grandTotal: String,
    val createdAt: Long,
)

data class StoredInvoiceLine(
    val id: String,
    val invoiceId: String,
    val sortOrder: Int,
    val description: String,
    val quantity: String,
    val unitPrice: String,
    val taxMode: String,
)

interface InvoiceRepository {
    fun observeInvoiceSummaries(): Flow<List<StoredInvoiceSummary>>

    suspend fun upsertInvoice(
        invoice: StoredInvoice,
        lines: List<StoredInvoiceLine>,
    )
}
