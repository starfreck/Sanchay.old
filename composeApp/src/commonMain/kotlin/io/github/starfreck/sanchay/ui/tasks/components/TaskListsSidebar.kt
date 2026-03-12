package io.github.starfreck.sanchay.ui.tasks.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.github.starfreck.sanchay.domain.model.TaskList
import io.github.starfreck.sanchay.ui.tasks.TasksViewModel

/**
 * Sidebar for switching between task lists.
 */
@Composable
fun TaskListsSidebar(
    selectedListId: Long?,
    taskLists: List<TaskList>,
    onListSelected: (Long?) -> Unit,
    onCreateList: (String) -> Unit,
    onDeleteList: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var newListText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(240.dp)
            .padding(12.dp)
    ) {
        Text(
            text = "Sanchay Tasks",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                SidebarItem(
                    icon = Icons.Outlined.LightMode,
                    label = "My Day",
                    selected = selectedListId == TasksViewModel.LIST_ID_MY_DAY,
                    onClick = { onListSelected(TasksViewModel.LIST_ID_MY_DAY) }
                )
            }
            
            item {
                SidebarItem(
                    icon = Icons.Outlined.StarBorder,
                    label = "Important",
                    selected = selectedListId == TasksViewModel.LIST_ID_IMPORTANT,
                    onClick = { onListSelected(TasksViewModel.LIST_ID_IMPORTANT) }
                )
            }

            item {
                SidebarItem(
                    icon = Icons.Outlined.CalendarToday,
                    label = "Planned",
                    selected = selectedListId == TasksViewModel.LIST_ID_PLANNED,
                    onClick = { onListSelected(TasksViewModel.LIST_ID_PLANNED) }
                )
            }

            item {
                SidebarItem(
                    icon = Icons.Default.AllInclusive,
                    label = "All Tasks",
                    selected = selectedListId == null || selectedListId == TasksViewModel.LIST_ID_ALL,
                    onClick = { onListSelected(null) }
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }

            items(taskLists, key = { it.id }) { list ->
                SidebarItem(
                    icon = Icons.AutoMirrored.Filled.List,
                    label = list.name,
                    selected = selectedListId == list.id,
                    onClick = { onListSelected(list.id) },
                    trailingIcon = {
                        IconButton(onClick = { onDeleteList(list.id) }) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete list", modifier = Modifier.size(18.dp))
                        }
                    }
                )
            }
        }

        // New List Entry
        Surface(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(36.dp),
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                    if (newListText.isEmpty()) {
                        Text(
                            text = "New list",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                    BasicTextField(
                        value = newListText,
                        onValueChange = { newListText = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        cursorBrush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.primary),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (newListText.isNotBlank()) {
                                    onCreateList(newListText)
                                    newListText = ""
                                }
                            }
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun SidebarItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Surface(
        selected = selected,
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )
            if (trailingIcon != null) {
                trailingIcon()
            }
        }
    }
}
