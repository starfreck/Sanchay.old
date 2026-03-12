package io.github.starfreck.sanchay.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes for the app.
 */
sealed interface Screen {

    /** Notes list / home screen. */
    @Serializable
    data object NotesList : Screen

    /** Note editor. [noteId] of 0 means create new. */
    @Serializable
    data class NoteEditor(val noteId: Long = 0L) : Screen

    /** Tasks list screen. */
    @Serializable
    data object TasksList : Screen

    /** Task editor. [taskId] of 0 means create new. */
    @Serializable
    data class TaskEditor(val taskId: Long = 0L) : Screen

    /** Settings screen. */
    @Serializable
    data object Settings : Screen
}
