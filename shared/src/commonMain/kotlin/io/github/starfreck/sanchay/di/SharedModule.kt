package io.github.starfreck.sanchay.di

import io.github.starfreck.sanchay.data.local.DatabaseProvider
import io.github.starfreck.sanchay.data.repository.NoteRepositoryImpl
import io.github.starfreck.sanchay.data.repository.TaskRepositoryImpl
import io.github.starfreck.sanchay.domain.repository.NoteRepository
import io.github.starfreck.sanchay.domain.repository.TaskRepository
import io.github.starfreck.sanchay.domain.usecase.*
import org.koin.dsl.module

/**
 * Koin module for the shared layer — database, repositories, and use cases.
 * Resides in the shared module so Room types stay off composeApp classpath.
 */
val sharedModule = module {

    // Database + DAOs (Room types encapsulated here)
    single { DatabaseProvider.getDatabase() }
    single { get<io.github.starfreck.sanchay.data.local.SanchayDatabase>().noteDao() }
    single { get<io.github.starfreck.sanchay.data.local.SanchayDatabase>().taskDao() }

    // Repositories
    single<NoteRepository> { NoteRepositoryImpl(get()) }
    single<TaskRepository> { TaskRepositoryImpl(get()) }

    // Note Use Cases
    factory { GetNotesUseCase(get()) }
    factory { GetNoteByIdUseCase(get()) }
    factory { SearchNotesUseCase(get()) }
    factory { GetArchivedNotesUseCase(get()) }
    factory { GetTrashedNotesUseCase(get()) }
    factory { SaveNoteUseCase(get()) }
    factory { DeleteNoteUseCase(get()) }

    // Task Use Cases
    factory { GetTasksUseCase(get()) }
    factory { GetTasksByListUseCase(get()) }
    factory { GetTaskByIdUseCase(get()) }
    factory { SaveTaskUseCase(get()) }
    factory { DeleteTaskUseCase(get()) }
    factory { ToggleTaskCompleteUseCase(get()) }
    factory { GetTaskListsUseCase(get()) }
    factory { SaveTaskListUseCase(get()) }
    factory { DeleteTaskListUseCase(get()) }
}
