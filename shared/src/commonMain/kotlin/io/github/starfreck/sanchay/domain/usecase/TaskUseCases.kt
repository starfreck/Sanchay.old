package io.github.starfreck.sanchay.domain.usecase

import io.github.starfreck.sanchay.domain.model.Task
import io.github.starfreck.sanchay.domain.model.TaskList
import io.github.starfreck.sanchay.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

/** Observe all tasks. */
class GetTasksUseCase(private val repository: TaskRepository) {
    operator fun invoke(): Flow<List<Task>> = repository.getAllTasks()
}

/** Observe tasks for a specific list. */
class GetTasksByListUseCase(private val repository: TaskRepository) {
    operator fun invoke(listId: Long): Flow<List<Task>> = repository.getTasksByList(listId)
}

/** Observe a single task by its ID. */
class GetTaskByIdUseCase(private val repository: TaskRepository) {
    operator fun invoke(id: Long): Flow<Task?> = repository.getTaskById(id)
}

/** Insert or update a task. */
class SaveTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task): Long = repository.upsertTask(task)
}

/** Delete a task permanently. */
class DeleteTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(id: Long) = repository.deleteTask(id)
}

/** Toggle task completion. */
class ToggleTaskCompleteUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(id: Long) = repository.toggleTaskComplete(id)
}

/** Observe all task lists. */
class GetTaskListsUseCase(private val repository: TaskRepository) {
    operator fun invoke(): Flow<List<TaskList>> = repository.getAllTaskLists()
}

/** Save a task list. */
class SaveTaskListUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(taskList: TaskList): Long = repository.upsertTaskList(taskList)
}

/** Delete a task list. */
class DeleteTaskListUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(id: Long) = repository.deleteTaskList(id)
}
