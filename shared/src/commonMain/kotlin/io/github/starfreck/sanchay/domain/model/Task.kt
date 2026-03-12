package io.github.starfreck.sanchay.domain.model

import kotlinx.serialization.Serializable
import kotlin.time.Clock

/**
 * Priority levels for tasks.
 */
@Serializable
enum class TaskPriority {
    NONE,
    LOW,
    MEDIUM,
    HIGH,
}

/**
 * Domain model representing a task.
 */
@Serializable
data class Task(
    val id: Long = 0L,
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val dueDateMillis: Long? = null,
    val priority: TaskPriority = TaskPriority.NONE,
    val listId: Long? = null,
    val isImportant: Boolean = false,
    val myDayDateMillis: Long? = null,
    val remindMeAtMillis: Long? = null,
    val steps: List<TaskStep> = emptyList(),
    val createdAtMillis: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAtMillis: Long = Clock.System.now().toEpochMilliseconds(),
)

/**
 * A subtask / step within a task.
 */
@Serializable
data class TaskStep(
    val id: Long = 0L,
    val title: String = "",
    val isCompleted: Boolean = false,
    val position: Int = 0,
)

/**
 * Domain model representing a task list (grouping).
 */
@Serializable
data class TaskList(
    val id: Long = 0L,
    val name: String = "",
    val createdAtMillis: Long = Clock.System.now().toEpochMilliseconds(),
)
