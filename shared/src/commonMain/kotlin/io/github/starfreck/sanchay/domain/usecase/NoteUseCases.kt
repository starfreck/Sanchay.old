package io.github.starfreck.sanchay.domain.usecase

import io.github.starfreck.sanchay.domain.model.Note
import io.github.starfreck.sanchay.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

/** Observe all active notes, with pinned notes first. */
class GetNotesUseCase(private val repository: NoteRepository) {
    operator fun invoke(): Flow<List<Note>> = repository.getAllNotes()
}

/** Observe a single note by its ID. */
class GetNoteByIdUseCase(private val repository: NoteRepository) {
    operator fun invoke(id: Long): Flow<Note?> = repository.getNoteById(id)
}

/** Search notes by query string. */
class SearchNotesUseCase(private val repository: NoteRepository) {
    operator fun invoke(query: String): Flow<List<Note>> = repository.searchNotes(query)
}

/** Observe archived notes. */
class GetArchivedNotesUseCase(private val repository: NoteRepository) {
    operator fun invoke(): Flow<List<Note>> = repository.getArchivedNotes()
}

/** Observe trashed notes. */
class GetTrashedNotesUseCase(private val repository: NoteRepository) {
    operator fun invoke(): Flow<List<Note>> = repository.getTrashedNotes()
}

/** Insert or update a note. */
class SaveNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note): Long = repository.upsertNote(note)
}

/** Delete a note permanently. */
class DeleteNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(id: Long) = repository.deleteNote(id)
}
