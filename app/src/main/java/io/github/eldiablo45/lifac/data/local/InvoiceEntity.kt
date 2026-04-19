package io.github.eldiablo45.lifac.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invoices")
data class InvoiceEntity(
    @PrimaryKey val id: String,
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
