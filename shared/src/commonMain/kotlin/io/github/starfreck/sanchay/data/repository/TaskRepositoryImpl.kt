package io.github.starfreck.sanchay.data.repository

import io.github.starfreck.sanchay.data.local.dao.TaskDao
import io.github.starfreck.sanchay.data.local.mapper.TaskMapper
import io.github.starfreck.sanchay.domain.model.Task
import io.github.starfreck.sanchay.domain.model.TaskList
import io.github.starfreck.sanchay.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

/**
 * Local-database-backed implementation of [TaskRepository].
 */
class TaskRepositoryImpl(
    private val taskDao: TaskDao,
) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> =
        taskDao.getAllTasksWithSteps().map { list -> list.map { TaskMapper.toDomain(it.task, it.steps) } }

    override fun getTasksByList(listId: Long): Flow<List<Task>> =
        taskDao.getTasksWithStepsByList(listId).map { list -> list.map { TaskMapper.toDomain(it.task, it.steps) } }

    override fun getTaskById(id: Long): Flow<Task?> =
        taskDao.getTaskWithStepsById(id).map { it?.let { relation -> TaskMapper.toDomain(relation.task, relation.steps) } }

    override suspend fun upsertTask(task: Task): Long {
        val taskId = taskDao.upsertTask(TaskMapper.toEntity(task))
        val resolvedId = if (task.id == 0L) taskId else task.id
        // Upsert steps
        taskDao.deleteStepsForTask(resolvedId)
        if (task.steps.isNotEmpty()) {
            taskDao.upsertSteps(task.steps.map { TaskMapper.stepToEntity(it, resolvedId) })
        }
        return resolvedId
    }

    override suspend fun deleteTask(id: Long) = taskDao.deleteTask(id)

    override suspend fun toggleTaskComplete(id: Long) {
        taskDao.toggleTaskComplete(id, Clock.System.now().toEpochMilliseconds())
    }

    // --- Task Lists ---

    override fun getAllTaskLists(): Flow<List<TaskList>> =
        taskDao.getAllTaskLists().map { entities -> entities.map(TaskMapper::taskListToDomain) }

    override suspend fun upsertTaskList(taskList: TaskList): Long =
        taskDao.upsertTaskList(TaskMapper.taskListToEntity(taskList))

    override suspend fun deleteTaskList(id: Long) =
        taskDao.deleteTaskListAndTasks(id)
}
