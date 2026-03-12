package io.github.starfreck.sanchay.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual fun getDatabaseBuilder(): RoomDatabase.Builder<SanchayDatabase> {
    val dbFilePath = NSHomeDirectory() + "/Documents/${SanchayDatabase.DATABASE_NAME}"
    return Room.databaseBuilder<SanchayDatabase>(
        name = dbFilePath,
    )
}
