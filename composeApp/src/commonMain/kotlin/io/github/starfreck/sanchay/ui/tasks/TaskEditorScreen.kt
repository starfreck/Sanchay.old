package io.github.starfreck.sanchay.ui.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.starfreck.sanchay.domain.model.TaskPriority
import io.github.starfreck.sanchay.ui.tasks.components.TaskStepItem
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.time.Clock

/**
 * Screen for creating or editing a task.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorScreen(
    taskId: Long,
    onBack: () -> Unit,
    viewModel: TaskEditorViewModel = koinViewModel { parametersOf(taskId) },
) {
    val task by viewModel.task.collectAsState()
    val isSaved by viewModel.isSaved.collectAsState()
    val taskLists by viewModel.taskLists.collectAsState()

    LaunchedEffect(isSaved) {
        if (isSaved) onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (taskId == 0L) "New Task" else "Edit Task") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (taskId != 0L) {
                        IconButton(onClick = viewModel::deleteTask) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                    IconButton(onClick = viewModel::saveTask) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            // --- Title ---
            TextField(
                value = task.title,
                onValueChange = viewModel::onTitleChanged,
                placeholder = { Text("Task title", style = MaterialTheme.typography.titleLarge) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.titleLarge,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                    unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                    focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                    unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                ),
                singleLine = true
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // --- Steps Section ---
            Text("Steps", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            task.steps.sortedBy { it.position }.forEach { step ->
                TaskStepItem(
                    step = step,
                    onToggle = { viewModel.onToggleStep(step.id) },
                    onRemove = { viewModel.onRemoveStep(step.id) },
                    onRename = { viewModel.onRenameStep(step.id, it) }
                )
            }

            var nextStepTitle by remember { mutableStateOf("") }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp).size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                TextField(
                    value = nextStepTitle,
                    onValueChange = { nextStepTitle = it },
                    placeholder = { Text("Next step") },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                        unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                        focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                        unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    singleLine = true
                )
                if (nextStepTitle.isNotBlank()) {
                    TextButton(onClick = {
                        viewModel.onAddStep(nextStepTitle)
                        nextStepTitle = ""
                    }) {
                        Text("Add")
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // --- Detail Actions ---
            DetailActionItem(
                icon = Icons.Default.WbSunny,
                label = if (task.myDayDateMillis != null) "Added to My Day" else "Add to My Day",
                selected = task.myDayDateMillis != null,
                onClick = viewModel::onToggleMyDay
            )

            DetailActionItem(
                icon = Icons.Default.NotificationsNone,
                label = "Remind me",
                value = task.remindMeAtMillis?.let { "At ${it}" }, // Placeholder for date formatter
                onClick = { /* Open reminder picker */ }
            )

            DetailActionItem(
                icon = Icons.Default.CalendarToday,
                label = "Add due date",
                value = task.dueDateMillis?.let { "Due ${it}" }, // Placeholder
                onClick = { /* Open due date picker */ }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // --- List & Priority Selectors ---
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // List Selector
                var showListMenu by remember { mutableStateOf(false) }
                val currentList = taskLists.find { it.id == task.listId }
                Surface(
                    onClick = { showListMenu = true },
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.List, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(currentList?.name ?: "Assign to list", style = MaterialTheme.typography.bodyMedium)
                    }
                    DropdownMenu(expanded = showListMenu, onDismissRequest = { showListMenu = false }) {
                        DropdownMenuItem(text = { Text("No List") }, onClick = { viewModel.onListSelected(null); showListMenu = false })
                        taskLists.forEach { list ->
                            DropdownMenuItem(text = { Text(list.name) }, onClick = { viewModel.onListSelected(list.id); showListMenu = false })
                        }
                    }
                }

                // Priority Selector
                var showPriorityMenu by remember { mutableStateOf(false) }
                Surface(
                    onClick = { showPriorityMenu = true },
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.PriorityHigh, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(task.priority.name, style = MaterialTheme.typography.bodyMedium)
                    }
                    DropdownMenu(expanded = showPriorityMenu, onDismissRequest = { showPriorityMenu = false }) {
                        TaskPriority.entries.forEach { p ->
                            DropdownMenuItem(text = { Text(p.name) }, onClick = { viewModel.onPriorityChanged(p); showPriorityMenu = false })
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // --- Description ---
            OutlinedTextField(
                value = task.description,
                onValueChange = viewModel::onDescriptionChanged,
                placeholder = { Text("Add note", style = MaterialTheme.typography.bodyMedium) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                shape = MaterialTheme.shapes.small,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun DetailActionItem(
    icon: ImageVector,
    label: String,
    selected: Boolean = false,
    value: String? = null,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = androidx.compose.ui.graphics.Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                if (value != null) {
                    Text(value, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                }
            }
            if (selected) {
                IconButton(onClick = onClick) {
                    Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
