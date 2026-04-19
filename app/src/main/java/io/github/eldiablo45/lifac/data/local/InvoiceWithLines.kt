package io.github.eldiablo45.lifac.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class InvoiceWithLines(
    @Embedded val invoice: InvoiceEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "invoiceId",
    )
    val lines: List<InvoiceLineEntity>,
)
