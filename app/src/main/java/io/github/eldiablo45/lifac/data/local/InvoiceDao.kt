package io.github.eldiablo45.lifac.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDao {
    @Query(
        """
        SELECT
            invoices.id AS id,
            invoices.number AS number,
            COALESCE(clients.displayName, 'Cliente pendiente') AS clientName,
            invoices.status AS status,
            invoices.grandTotal AS total,
            invoices.issueDate AS issueDate,
            invoices.createdAt AS createdAt
        FROM invoices
        LEFT JOIN clients ON clients.id = invoices.clientId
        ORDER BY invoices.createdAt DESC
        """,
    )
    fun observeInvoiceSummaries(): Flow<List<InvoiceSummaryRow>>

    @Upsert
    suspend fun upsertInvoice(invoice: InvoiceEntity)

    @Upsert
    suspend fun upsertLines(lines: List<InvoiceLineEntity>)

    @Query("DELETE FROM invoice_lines WHERE invoiceId = :invoiceId")
    suspend fun deleteLinesByInvoiceId(invoiceId: String)
}
