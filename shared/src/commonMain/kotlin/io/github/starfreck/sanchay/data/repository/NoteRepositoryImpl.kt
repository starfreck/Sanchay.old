package io.github.starfreck.sanchay.data.repository

import io.github.starfreck.sanchay.data.local.dao.NoteDao
import io.github.starfreck.sanchay.data.local.mapper.NoteMapper
import io.github.starfreck.sanchay.domain.model.Note
import io.github.starfreck.sanchay.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Local-database-backed implementation of [NoteRepository].
 * When API support is added, this class becomes the local-cache layer.
 */
class NoteRepositoryImpl(
    private val noteDao: NoteDao,
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> =
        noteDao.getAllActiveNotes().map { entities -> entities.map(NoteMapper::toDomain) }

    override fun getNoteById(id: Long): Flow<Note?> =
        noteDao.getNoteById(id).map { it?.let(NoteMapper::toDomain) }

    override fun searchNotes(query: String): Flow<List<Note>> =
        noteDao.searchNotes(query).map { entities -> entities.map(NoteMapper::toDomain) }

    override fun getArchivedNotes(): Flow<List<Note>> =
        noteDao.getArchivedNotes().map { entities -> entities.map(NoteMapper::toDomain) }

    override fun getTrashedNotes(): Flow<List<Note>> =
        noteDao.getTrashedNotes().map { entities -> entities.map(NoteMapper::toDomain) }

    override suspend fun upsertNote(note: Note): Long =
        noteDao.upsertNote(NoteMapper.toEntity(note))

    override suspend fun deleteNote(id: Long) =
        noteDao.deleteNote(id)
}
