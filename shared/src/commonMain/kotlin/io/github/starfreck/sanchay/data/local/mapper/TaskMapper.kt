package io.github.starfreck.sanchay.data.local.mapper

import io.github.starfreck.sanchay.data.local.entity.TaskEntity
import io.github.starfreck.sanchay.data.local.entity.TaskListEntity
import io.github.starfreck.sanchay.data.local.entity.TaskStepEntity
import io.github.starfreck.sanchay.domain.model.Task
import io.github.starfreck.sanchay.domain.model.TaskList
import io.github.starfreck.sanchay.domain.model.TaskPriority
import io.github.starfreck.sanchay.domain.model.TaskStep

/**
 * Maps between Task entities and domain models.
 */
object TaskMapper {

    fun toDomain(entity: TaskEntity, steps: List<TaskStepEntity> = emptyList()): Task = Task(
        id = entity.id,
        title = entity.title,
        description = entity.description,
        isCompleted = entity.isCompleted,
        dueDateMillis = entity.dueDate,
        priority = try { TaskPriority.valueOf(entity.priority) } catch (_: Exception) { TaskPriority.NONE },
        listId = entity.listId,
        isImportant = entity.isImportant,
        myDayDateMillis = entity.myDayDateMillis,
        remindMeAtMillis = entity.remindMeAtMillis,
        steps = steps.map { stepToDomain(it) },
        createdAtMillis = entity.createdAt,
        updatedAtMillis = entity.updatedAt,
    )

    fun toEntity(task: Task): TaskEntity = TaskEntity(
        id = task.id,
        title = task.title,
        description = task.description,
        isCompleted = task.isCompleted,
        dueDate = task.dueDateMillis,
        priority = task.priority.name,
        listId = task.listId,
        isImportant = task.isImportant,
        myDayDateMillis = task.myDayDateMillis,
        remindMeAtMillis = task.remindMeAtMillis,
        createdAt = task.createdAtMillis,
        updatedAt = task.updatedAtMillis,
    )

    fun stepToDomain(entity: TaskStepEntity): TaskStep = TaskStep(
        id = entity.id,
        title = entity.title,
        isCompleted = entity.isCompleted,
        position = entity.position,
    )

    fun stepToEntity(step: TaskStep, taskId: Long): TaskStepEntity = TaskStepEntity(
        id = step.id,
        taskId = taskId,
        title = step.title,
        isCompleted = step.isCompleted,
        position = step.position,
    )

    fun taskListToDomain(entity: TaskListEntity): TaskList = TaskList(
        id = entity.id,
        name = entity.name,
        createdAtMillis = entity.createdAt,
    )

    fun taskListToEntity(taskList: TaskList): TaskListEntity = TaskListEntity(
        id = taskList.id,
        name = taskList.name,
        createdAt = taskList.createdAtMillis,
    )
}
