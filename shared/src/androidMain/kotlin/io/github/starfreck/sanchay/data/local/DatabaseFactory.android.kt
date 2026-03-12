package io.github.starfreck.sanchay.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

private lateinit var appContext: Context

/**
 * Call this once from Application.onCreate or Activity to provide Context.
 */
fun initializeAndroidDatabaseContext(context: Context) {
    appContext = context.applicationContext
}

actual fun getDatabaseBuilder(): RoomDatabase.Builder<SanchayDatabase> {
    val dbFile = appContext.getDatabasePath(SanchayDatabase.DATABASE_NAME)
    return Room.databaseBuilder<SanchayDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}
