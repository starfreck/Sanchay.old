package io.github.starfreck.sanchay.data.local

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

/**
 * Creates and provides the [SanchayDatabase] instance.
 * This lives in the shared module so composeApp doesn't need Room on its classpath.
 */
object DatabaseProvider {
    
    private var database: SanchayDatabase? = null

    fun getDatabase(): SanchayDatabase {
        return database ?: getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .fallbackToDestructiveMigration(true)
            .build()
            .also { database = it }
    }
}
