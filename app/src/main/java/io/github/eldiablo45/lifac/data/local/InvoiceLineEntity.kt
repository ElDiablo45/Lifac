package io.github.eldiablo45.lifac.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "invoice_lines",
    foreignKeys = [
        ForeignKey(
            entity = InvoiceEntity::class,
            parentColumns = ["id"],
            childColumns = ["invoiceId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["invoiceId"])],
)
data class InvoiceLineEntity(
    @PrimaryKey val id: String,
    val invoiceId: String,
    val sortOrder: Int,
    val description: String,
    val quantity: String,
    val unitPrice: String,
    val taxMode: String,
)
