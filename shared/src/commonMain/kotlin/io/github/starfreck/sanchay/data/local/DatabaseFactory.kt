package io.github.starfreck.sanchay.data.local

import androidx.room.RoomDatabase

/**
 * Expect declaration for platform-specific database builder creation.
 * Each platform provides its own [RoomDatabase.Builder] implementation.
 */
expect fun getDatabaseBuilder(): RoomDatabase.Builder<SanchayDatabase>
