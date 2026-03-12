package io.github.starfreck.sanchay.domain.repository

import io.github.starfreck.sanchay.domain.model.Task
import io.github.starfreck.sanchay.domain.model.TaskList
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for task operations.
 */
interface TaskRepository {

    /** Observe all tasks. */
    fun getAllTasks(): Flow<List<Task>>

    /** Observe tasks filtered by list. */
    fun getTasksByList(listId: Long): Flow<List<Task>>

    /** Observe a single task by ID. */
    fun getTaskById(id: Long): Flow<Task?>

    /** Insert or update a task. Returns the inserted row ID. */
    suspend fun upsertTask(task: Task): Long

    /** Delete a task permanently. */
    suspend fun deleteTask(id: Long)

    /** Toggle the completion status of a task. */
    suspend fun toggleTaskComplete(id: Long)

    // --- Task Lists ---

    /** Observe all task lists. */
    fun getAllTaskLists(): Flow<List<TaskList>>

    /** Insert or update a task list. Returns the inserted row ID. */
    suspend fun upsertTaskList(taskList: TaskList): Long

    /** Delete a task list and optionally its tasks. */
    suspend fun deleteTaskList(id: Long)
}
