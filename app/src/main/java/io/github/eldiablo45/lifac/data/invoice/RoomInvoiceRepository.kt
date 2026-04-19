package io.github.eldiablo45.lifac.data.invoice

import android.content.Context
import androidx.room.withTransaction
import io.github.eldiablo45.lifac.data.local.InvoiceEntity
import io.github.eldiablo45.lifac.data.local.InvoiceLineEntity
import io.github.eldiablo45.lifac.data.local.InvoiceSummaryRow
import io.github.eldiablo45.lifac.data.local.LifacDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomInvoiceRepository(
    private val database: LifacDatabase,
) : InvoiceRepository {
    override fun observeInvoiceSummaries(): Flow<List<StoredInvoiceSummary>> {
        return database.invoiceDao().observeInvoiceSummaries().map { rows ->
            rows.map { row -> row.toStoredSummary() }
        }
    }

    override suspend fun upsertInvoice(
        invoice: StoredInvoice,
        lines: List<StoredInvoiceLine>,
    ) {
        database.withTransaction {
            database.invoiceDao().upsertInvoice(
                InvoiceEntity(
                    id = invoice.id,
                    clientId = invoice.clientId,
                    status = invoice.status,
                    series = invoice.series,
                    number = invoice.number,
                    issueDate = invoice.issueDate,
                    operationDate = invoice.operationDate,
                    projectLabel = invoice.projectLabel,
                    notes = invoice.notes,
                    taxMode = invoice.taxMode,
                    subtotal = invoice.subtotal,
                    taxTotal = invoice.taxTotal,
                    grandTotal = invoice.grandTotal,
                    createdAt = invoice.createdAt,
                ),
            )
            database.invoiceDao().deleteLinesByInvoiceId(invoice.id)
            if (lines.isNotEmpty()) {
                database.invoiceDao().upsertLines(
                    lines.map { line ->
                        InvoiceLineEntity(
                            id = line.id,
                            invoiceId = line.invoiceId,
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
        fun from(context: Context): RoomInvoiceRepository {
            return RoomInvoiceRepository(LifacDatabase.getInstance(context))
        }
    }
}

private fun InvoiceSummaryRow.toStoredSummary(): StoredInvoiceSummary {
    return StoredInvoiceSummary(
        id = id,
        number = number,
        clientName = clientName,
        status = status,
        total = total,
        issueDate = issueDate,
        createdAt = createdAt,
    )
}
