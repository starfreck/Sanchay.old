package io.github.starfreck.sanchay.domain.model

import kotlinx.serialization.Serializable
import kotlin.time.Clock

/**
 * Colors for notes.
 */
@Serializable
enum class NoteColor(val hexValue: Long = 0xFFFFFFFF) {
    DEFAULT(0xFFFFFFFF),
    CORAL(0xFFFF8B94),
    PEACH(0xFFFFD3B6),
    SAND(0xFFFFAA64),
    MINT(0xFF88D8B0),
    SAGE(0xFFB9E1CC),
    FOG(0xFFA8E6CE),
    STORM(0xFF679186),
    DUSK(0xFF264653),
    BLOSSOM(0xFFE76F51),
    CLAY(0xFF2A9D8F),
    CHALK(0xFFE9C46A),
}

/**
 * Domain model representing a note.
 */
@Serializable
data class Note(
    val id: Long = 0L,
    val title: String = "",
    val content: String = "",
    val color: NoteColor = NoteColor.DEFAULT,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val isTrashed: Boolean = false,
    val labels: List<String> = emptyList(),
    val createdAtMillis: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAtMillis: Long = Clock.System.now().toEpochMilliseconds(),
)
