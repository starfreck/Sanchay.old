package io.github.starfreck.sanchay.data.local.mapper

import io.github.starfreck.sanchay.data.local.entity.NoteEntity
import io.github.starfreck.sanchay.domain.model.Note
import io.github.starfreck.sanchay.domain.model.NoteColor
import kotlinx.serialization.json.Json

/**
 * Maps between [NoteEntity] and [Note] domain model.
 */
object NoteMapper {

    fun toDomain(entity: NoteEntity): Note = Note(
        id = entity.id,
        title = entity.title,
        content = entity.content,
        color = try { NoteColor.valueOf(entity.colorName) } catch (_: Exception) { NoteColor.DEFAULT },
        isPinned = entity.isPinned,
        isArchived = entity.isArchived,
        isTrashed = entity.isTrashed,
        labels = try { Json.decodeFromString<List<String>>(entity.labelsJson) } catch (_: Exception) { emptyList() },
        createdAtMillis = entity.createdAt,
        updatedAtMillis = entity.updatedAt,
    )

    fun toEntity(note: Note): NoteEntity = NoteEntity(
        id = note.id,
        title = note.title,
        content = note.content,
        colorName = note.color.name,
        isPinned = note.isPinned,
        isArchived = note.isArchived,
        isTrashed = note.isTrashed,
        labelsJson = Json.encodeToString(note.labels),
        createdAt = note.createdAtMillis,
        updatedAt = note.updatedAtMillis,
    )
}
