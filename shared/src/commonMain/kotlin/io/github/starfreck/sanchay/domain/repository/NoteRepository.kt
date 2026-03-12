package io.github.starfreck.sanchay.domain.repository

import io.github.starfreck.sanchay.domain.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for note operations.
 * Implementations can back this with local DB or remote API.
 */
interface NoteRepository {

    /** Observe all active (non-archived, non-trashed) notes. */
    fun getAllNotes(): Flow<List<Note>>

    /** Observe a single note by ID. */
    fun getNoteById(id: Long): Flow<Note?>

    /** Search notes by title or content. */
    fun searchNotes(query: String): Flow<List<Note>>

    /** Observe archived notes. */
    fun getArchivedNotes(): Flow<List<Note>>

    /** Observe trashed notes. */
    fun getTrashedNotes(): Flow<List<Note>>

    /** Insert or update a note. Returns the inserted row ID. */
    suspend fun upsertNote(note: Note): Long

    /** Delete a note permanently. */
    suspend fun deleteNote(id: Long)
}
