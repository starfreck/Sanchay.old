package io.github.starfreck.sanchay.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.starfreck.sanchay.domain.model.Task
import io.github.starfreck.sanchay.domain.model.TaskList
import io.github.starfreck.sanchay.domain.model.TaskPriority
import io.github.starfreck.sanchay.domain.usecase.DeleteTaskUseCase
import io.github.starfreck.sanchay.domain.usecase.GetTaskByIdUseCase
import io.github.starfreck.sanchay.domain.usecase.GetTaskListsUseCase
import io.github.starfreck.sanchay.domain.usecase.SaveTaskUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Clock

/**
 * ViewModel for creating and editing a single task.
 */
class TaskEditorViewModel(
    private val taskId: Long,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getTaskListsUseCase: GetTaskListsUseCase,
) : ViewModel() {

    private val _task = MutableStateFlow(Task())
    val task = _task.asStateFlow()

    private val _isSaved = MutableStateFlow(false)
    val isSaved = _isSaved.asStateFlow()

    val taskLists: StateFlow<List<TaskList>> = getTaskListsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        if (taskId != 0L) {
            viewModelScope.launch {
                getTaskByIdUseCase(taskId).collect { loadedTask ->
                    if (loadedTask != null) {
                        _task.value = loadedTask
                    }
                }
            }
        }
    }

    fun onTitleChanged(title: String) {
        _task.value = _task.value.copy(title = title)
    }

    fun onDescriptionChanged(description: String) {
        _task.value = _task.value.copy(description = description)
    }

    fun onPriorityChanged(priority: TaskPriority) {
        _task.value = _task.value.copy(priority = priority)
    }

    fun onDueDateChanged(dueDate: Long?) {
        _task.value = _task.value.copy(dueDateMillis = dueDate)
    }

    fun onListSelected(listId: Long?) {
        _task.value = _task.value.copy(listId = listId)
    }

    fun onToggleMyDay() {
        val current = _task.value.myDayDateMillis
        val newValue = if (current == null) Clock.System.now().toEpochMilliseconds() else null
        _task.value = _task.value.copy(myDayDateMillis = newValue)
    }

    fun onToggleImportant() {
        _task.value = _task.value.copy(isImportant = !_task.value.isImportant)
    }

    fun onReminderChanged(millis: Long?) {
        _task.value = _task.value.copy(remindMeAtMillis = millis)
    }

    // --- Steps ---

    fun onAddStep(title: String) {
        if (title.isBlank()) return
        val currentSteps = _task.value.steps
        val newStep = io.github.starfreck.sanchay.domain.model.TaskStep(
            id = if (currentSteps.isEmpty()) -1L else (currentSteps.minOf { it.id } - 1L).coerceAtMost(-1L),
            title = title,
            position = currentSteps.size
        )
        _task.value = _task.value.copy(steps = currentSteps + newStep)
    }

    fun onToggleStep(stepId: Long) {
        val currentSteps = _task.value.steps
        val updatedSteps = currentSteps.map {
            if (it.id == stepId) it.copy(isCompleted = !it.isCompleted) else it
        }
        _task.value = _task.value.copy(steps = updatedSteps)
    }

    fun onRemoveStep(stepId: Long) {
        _task.value = _task.value.copy(steps = _task.value.steps.filter { it.id != stepId })
    }

    fun onRenameStep(stepId: Long, newTitle: String) {
        val currentSteps = _task.value.steps
        val updatedSteps = currentSteps.map {
            if (it.id == stepId) it.copy(title = newTitle) else it
        }
        _task.value = _task.value.copy(steps = updatedSteps)
    }

    fun saveTask() {
        viewModelScope.launch {
            saveTaskUseCase(_task.value)
            _isSaved.value = true
        }
    }

    fun deleteTask() {
        if (taskId != 0L) {
            viewModelScope.launch {
                deleteTaskUseCase(taskId)
                _isSaved.value = true
            }
        }
    }
}
