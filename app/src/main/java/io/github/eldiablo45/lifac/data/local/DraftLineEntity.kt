package io.github.eldiablo45.lifac.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "draft_lines",
    foreignKeys = [
        ForeignKey(
            entity = InvoiceDraftEntity::class,
            parentColumns = ["id"],
            childColumns = ["draftId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["draftId"])],
)
data class DraftLineEntity(
    @PrimaryKey val id: String,
    val draftId: String,
    val sortOrder: Int,
    val description: String,
    val quantity: String,
    val unitPrice: String,
    val taxMode: String,
)
