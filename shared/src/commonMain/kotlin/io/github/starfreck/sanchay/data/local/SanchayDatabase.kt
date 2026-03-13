package io.github.starfreck.sanchay.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import io.github.starfreck.sanchay.data.local.dao.NoteDao
import io.github.starfreck.sanchay.data.local.dao.TaskDao
import io.github.starfreck.sanchay.data.local.entity.NoteEntity
import io.github.starfreck.sanchay.data.local.entity.TaskEntity
import io.github.starfreck.sanchay.data.local.entity.TaskListEntity
import io.github.starfreck.sanchay.data.local.entity.TaskStepEntity

/**
 * Room database for the Sanchay app.
 */
@Database(
    entities = [
        NoteEntity::class,
        TaskEntity::class,
        TaskListEntity::class,
        TaskStepEntity::class,
    ],
    version = 4,
    exportSchema = true,
)
@ConstructedBy(SanchayDatabaseConstructor::class)
abstract class SanchayDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun taskDao(): TaskDao

    companion object {
        const val DATABASE_NAME = "sanchay.db"
    }
}

// The Room compiler generates the implementation of this class
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object SanchayDatabaseConstructor : RoomDatabaseConstructor<SanchayDatabase>
