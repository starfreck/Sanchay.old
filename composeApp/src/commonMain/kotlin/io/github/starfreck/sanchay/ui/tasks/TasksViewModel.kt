package io.github.starfreck.sanchay.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.starfreck.sanchay.domain.model.Task
import io.github.starfreck.sanchay.domain.model.TaskList
import io.github.starfreck.sanchay.domain.usecase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Clock

/**
 * ViewModel for the Tasks list screen.
 */
class TasksViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val getTaskListsUseCase: GetTaskListsUseCase,
    private val toggleTaskCompleteUseCase: ToggleTaskCompleteUseCase,
    private val deleteUseCase: DeleteTaskUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val saveTaskListUseCase: SaveTaskListUseCase,
    private val deleteTaskListUseCase: DeleteTaskListUseCase,
) : ViewModel() {

    companion object {
        const val LIST_ID_ALL = 0L
        const val LIST_ID_MY_DAY = -1L
        const val LIST_ID_IMPORTANT = -2L
        const val LIST_ID_PLANNED = -3L
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedListId = MutableStateFlow<Long?>(null)
    val selectedListId = _selectedListId.asStateFlow()

    private val _isListSettingsOpen = MutableStateFlow(false)
    val isListSettingsOpen = _isListSettingsOpen.asStateFlow()

    val taskLists: StateFlow<List<TaskList>> = getTaskListsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tasks: StateFlow<List<Task>> = combine(
        getTasksUseCase(),
        _searchQuery,
        _selectedListId
    ) { allTasks, query, listId ->
        allTasks.filter { task ->
            val matchesQuery = task.title.contains(query, ignoreCase = true) ||
                    task.description.contains(query, ignoreCase = true)
            
            val matchesList = when (listId) {
                null, LIST_ID_ALL -> true
                LIST_ID_MY_DAY -> task.myDayDateMillis != null
                LIST_ID_IMPORTANT -> task.isImportant
                LIST_ID_PLANNED -> task.dueDateMillis != null
                else -> task.listId == listId
            }
            
            matchesQuery && matchesList
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onListSelected(listId: Long?) {
        _selectedListId.value = listId
    }

    fun onQuickAddTask(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            val listId = when (val current = _selectedListId.value) {
                null, LIST_ID_ALL, LIST_ID_MY_DAY, LIST_ID_IMPORTANT, LIST_ID_PLANNED -> null
                else -> current
            }
            
            val task = Task(
                title = title,
                listId = listId,
                isImportant = _selectedListId.value == LIST_ID_IMPORTANT,
                myDayDateMillis = if (_selectedListId.value == LIST_ID_MY_DAY) Clock.System.now().toEpochMilliseconds() else null
            )
            saveTaskUseCase(task)
        }
    }

    fun onToggleTaskComplete(taskId: Long) {
        viewModelScope.launch {
            toggleTaskCompleteUseCase(taskId)
        }
    }

    fun onToggleTaskImportant(taskId: Long) {
        viewModelScope.launch {
            val task = tasks.value.find { it.id == taskId } ?: return@launch
            saveTaskUseCase(task.copy(isImportant = !task.isImportant))
        }
    }

    fun onDeleteTask(taskId: Long) {
        viewModelScope.launch {
            deleteUseCase(taskId)
        }
    }

    // --- List CRUD ---

    fun onCreateList(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            val id = saveTaskListUseCase(TaskList(name = name))
            _selectedListId.value = id
        }
    }

    fun onRenameList(listId: Long, newName: String) {
        if (newName.isBlank()) return
        viewModelScope.launch {
            val list = taskLists.value.find { it.id == listId } ?: return@launch
            saveTaskListUseCase(list.copy(name = newName))
        }
    }

    fun onDeleteList(listId: Long) {
        viewModelScope.launch {
            deleteTaskListUseCase(listId)
            if (_selectedListId.value == listId) {
                _selectedListId.value = null
            }
        }
    }
}
