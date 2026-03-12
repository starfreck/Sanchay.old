package io.github.starfreck.sanchay.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.starfreck.sanchay.domain.model.Note
import io.github.starfreck.sanchay.domain.usecase.DeleteNoteUseCase
import io.github.starfreck.sanchay.domain.usecase.GetNotesUseCase
import io.github.starfreck.sanchay.domain.usecase.SearchNotesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for the Notes list screen.
 */
class NotesViewModel(
    private val getNotesUseCase: GetNotesUseCase,
    private val searchNotesUseCase: SearchNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val notes: StateFlow<List<Note>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                getNotesUseCase()
            } else {
                searchNotesUseCase(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            deleteNoteUseCase(id)
        }
    }
}
