package io.github.starfreck.sanchay.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity for the `task_lists` table.
 */
@Entity(tableName = "task_lists")
data class TaskListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String = "",
    val createdAt: Long = 0L,
)

/**
 * Room entity for the `tasks` table.
 */
@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = TaskListEntity::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.SET_NULL,
        )
    ],
    indices = [Index("listId")],
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val dueDate: Long? = null,
    val priority: String = "NONE",
    val listId: Long? = null,
    val isImportant: Boolean = false,
    val myDayDateMillis: Long? = null,
    val remindMeAtMillis: Long? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
)

/**
 * Relation class to fetch a task with all its steps.
 */
data class TaskWithSteps(
    @androidx.room.Embedded
    val task: TaskEntity,
    @androidx.room.Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val steps: List<TaskStepEntity>
)

/**
 * Room entity for the `task_steps` table (subtasks).
 */
@Entity(
    tableName = "task_steps",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index("taskId")],
)
data class TaskStepEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val taskId: Long,
    val title: String = "",
    val isCompleted: Boolean = false,
    val position: Int = 0,
)
