package io.github.starfreck.sanchay.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.starfreck.sanchay.domain.model.Note
import io.github.starfreck.sanchay.domain.model.NoteColor
import io.github.starfreck.sanchay.domain.usecase.GetNoteByIdUseCase
import io.github.starfreck.sanchay.domain.usecase.SaveNoteUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Clock

/**
 * ViewModel for the Note editor screen.
 */
class NoteEditorViewModel(
    private val noteId: Long,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
) : ViewModel() {

    private val _note = MutableStateFlow(Note())
    val note: StateFlow<Note> = _note.asStateFlow()

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved.asStateFlow()

    init {
        if (noteId != 0L) {
            viewModelScope.launch {
                getNoteByIdUseCase(noteId).firstOrNull()?.let { existing ->
                    _note.value = existing
                }
            }
        }
    }

    fun onTitleChanged(title: String) {
        _note.update { it.copy(title = title) }
    }

    fun onContentChanged(content: String) {
        _note.update { it.copy(content = content) }
    }

    fun onColorChanged(color: NoteColor) {
        _note.update { it.copy(color = color) }
    }

    fun onPinnedToggled() {
        _note.update { it.copy(isPinned = !it.isPinned) }
    }

    fun saveNote() {
        viewModelScope.launch {
            val now = Clock.System.now().toEpochMilliseconds()
            val noteToSave = _note.value.copy(
                updatedAtMillis = now,
                createdAtMillis = if (_note.value.id == 0L) now else _note.value.createdAtMillis,
            )
            saveNoteUseCase(noteToSave)
            _isSaved.value = true
        }
    }
}
