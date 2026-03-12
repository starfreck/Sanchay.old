package io.github.starfreck.sanchay.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for the `notes` table.
 */
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String = "",
    val content: String = "",
    val colorName: String = "DEFAULT",
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val isTrashed: Boolean = false,
    val labelsJson: String = "[]",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
)
