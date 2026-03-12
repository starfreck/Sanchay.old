package io.github.starfreck.sanchay.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.github.starfreck.sanchay.ui.notes.NoteEditorScreen
import io.github.starfreck.sanchay.ui.notes.NotesListScreen
import io.github.starfreck.sanchay.ui.tasks.TaskEditorScreen
import io.github.starfreck.sanchay.ui.tasks.TasksListScreen

/**
 * Top-level navigation host for the app.
 */
@Composable
fun SanchayNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.TasksList,
        modifier = modifier.fillMaxSize(),
    ) {
        // Notes
        composable<Screen.NotesList> {
            NotesListScreen(
                onNoteClick = { noteId -> navController.navigate(Screen.NoteEditor(noteId)) },
                onCreateNote = { navController.navigate(Screen.NoteEditor()) },
                onOpenSettings = { navController.navigate(Screen.Settings) }
            )
        }

        composable<Screen.NoteEditor> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.NoteEditor>()
            NoteEditorScreen(
                noteId = route.noteId,
                onBack = { navController.popBackStack() },
            )
        }

        composable<Screen.TasksList> {
            TasksListScreen(
                onTaskClick = { taskId -> navController.navigate(Screen.TaskEditor(taskId)) },
                onCreateTask = { navController.navigate(Screen.TaskEditor()) },
                onOpenSettings = { navController.navigate(Screen.Settings) }
            )
        }

        composable<Screen.TaskEditor> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.TaskEditor>()
            TaskEditorScreen(
                taskId = route.taskId,
                onBack = { navController.popBackStack() },
            )
        }

        // Calendar
        composable<Screen.Calendar> {
            io.github.starfreck.sanchay.ui.calendar.CalendarScreen(
                onOpenSettings = { navController.navigate(Screen.Settings) }
            )
        }

        // Settings
        composable<Screen.Settings> {
            Text("Settings — Coming Soon")
        }
    }
}
