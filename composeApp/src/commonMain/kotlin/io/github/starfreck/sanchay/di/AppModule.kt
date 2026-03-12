package io.github.starfreck.sanchay.di

import io.github.starfreck.sanchay.ui.notes.NoteEditorViewModel
import io.github.starfreck.sanchay.ui.notes.NotesViewModel
import io.github.starfreck.sanchay.ui.tasks.TaskEditorViewModel
import io.github.starfreck.sanchay.ui.tasks.TasksViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for the composeApp layer — ViewModels only.
 * Uses shared module's use cases (no Room types here).
 */
val appModule = module {
    // ViewModels (use cases injected from sharedModule)
    viewModel { NotesViewModel(get(), get(), get()) }
    viewModel { params -> NoteEditorViewModel(params.get(), get(), get()) }
    
    viewModel { TasksViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { params -> TaskEditorViewModel(params.get(), get(), get(), get(), get()) }
}
