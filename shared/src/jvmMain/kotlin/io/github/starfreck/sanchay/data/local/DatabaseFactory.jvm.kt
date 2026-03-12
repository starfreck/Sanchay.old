package io.github.starfreck.sanchay.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<SanchayDatabase> {
    val dbDir = File(System.getProperty("user.home"), ".sanchay")
    if (!dbDir.exists()) dbDir.mkdirs()
    val dbFile = File(dbDir, SanchayDatabase.DATABASE_NAME)
    return Room.databaseBuilder<SanchayDatabase>(
        name = dbFile.absolutePath,
    )
}
