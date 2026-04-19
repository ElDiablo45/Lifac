package io.github.eldiablo45.lifac.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "concepts")
data class ConceptEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val unitPrice: String,
    val taxMode: String,
)
