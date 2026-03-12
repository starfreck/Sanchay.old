package io.github.starfreck.sanchay.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import io.github.starfreck.sanchay.data.local.entity.TaskEntity
import io.github.starfreck.sanchay.data.local.entity.TaskListEntity
import io.github.starfreck.sanchay.data.local.entity.TaskStepEntity
import io.github.starfreck.sanchay.data.local.entity.TaskWithSteps
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for task operations.
 */
@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY isCompleted ASC, updatedAt DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE listId = :listId ORDER BY isCompleted ASC, updatedAt DESC")
    fun getTasksWithStepsByList(listId: Long): Flow<List<TaskWithSteps>>

    @Transaction
    @Query("SELECT * FROM tasks ORDER BY isCompleted ASC, updatedAt DESC")
    fun getAllTasksWithSteps(): Flow<List<TaskWithSteps>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskWithStepsById(id: Long): Flow<TaskWithSteps?>

    @Upsert
    suspend fun upsertTask(task: TaskEntity): Long

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: Long)

    @Query("UPDATE tasks SET isCompleted = NOT isCompleted, updatedAt = :updatedAt WHERE id = :id")
    suspend fun toggleTaskComplete(id: Long, updatedAt: Long)

    // --- Task Steps ---

    @Query("SELECT * FROM task_steps WHERE taskId = :taskId ORDER BY position ASC")
    fun getStepsForTask(taskId: Long): Flow<List<TaskStepEntity>>

    @Upsert
    suspend fun upsertSteps(steps: List<TaskStepEntity>)

    @Query("DELETE FROM task_steps WHERE taskId = :taskId")
    suspend fun deleteStepsForTask(taskId: Long)

    // --- Task Lists ---

    @Query("SELECT * FROM task_lists ORDER BY name ASC")
    fun getAllTaskLists(): Flow<List<TaskListEntity>>

    @Upsert
    suspend fun upsertTaskList(taskList: TaskListEntity): Long

    @Transaction
    suspend fun deleteTaskListAndTasks(listId: Long) {
        // Tasks with this listId will have listId set to null via FK SET_NULL
        deleteTaskListById(listId)
    }

    @Query("DELETE FROM task_lists WHERE id = :id")
    suspend fun deleteTaskListById(id: Long)
}
