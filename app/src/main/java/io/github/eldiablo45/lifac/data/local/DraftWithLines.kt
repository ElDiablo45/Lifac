package io.github.eldiablo45.lifac.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class DraftWithLines(
    @Embedded val draft: InvoiceDraftEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "draftId",
    )
    val lines: List<DraftLineEntity>,
)
