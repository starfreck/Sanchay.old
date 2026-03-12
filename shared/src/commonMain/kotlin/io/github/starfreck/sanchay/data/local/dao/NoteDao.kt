package io.github.starfreck.sanchay.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.github.starfreck.sanchay.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for note operations.
 */
@Dao
interface NoteDao {

    @Query("SELECT * FROM notes WHERE isArchived = 0 AND isTrashed = 0 ORDER BY isPinned DESC, updatedAt DESC")
    fun getAllActiveNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById(id: Long): Flow<NoteEntity?>

    @Query("SELECT * FROM notes WHERE (title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%') AND isArchived = 0 AND isTrashed = 0 ORDER BY updatedAt DESC")
    fun searchNotes(query: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE isArchived = 1 ORDER BY updatedAt DESC")
    fun getArchivedNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE isTrashed = 1 ORDER BY updatedAt DESC")
    fun getTrashedNotes(): Flow<List<NoteEntity>>

    @Upsert
    suspend fun upsertNote(note: NoteEntity): Long

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: Long)
}
