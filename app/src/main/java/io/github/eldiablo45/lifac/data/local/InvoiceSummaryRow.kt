package io.github.eldiablo45.lifac.data.local

data class InvoiceSummaryRow(
    val id: String,
    val number: String,
    val clientName: String,
    val status: String,
    val total: String,
    val issueDate: String,
    val createdAt: Long,
)
