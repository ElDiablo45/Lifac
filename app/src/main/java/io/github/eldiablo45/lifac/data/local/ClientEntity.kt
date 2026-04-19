package io.github.eldiablo45.lifac.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey val id: String,
    val kind: String,
    val displayName: String,
    val taxId: String,
    val city: String,
    val address: String,
    val notes: String,
)
