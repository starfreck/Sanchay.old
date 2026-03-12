package io.github.starfreck.sanchay.ui.notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.starfreck.sanchay.domain.model.Note
import io.github.starfreck.sanchay.domain.model.NoteColor
import io.github.starfreck.sanchay.theme.toColor
import org.koin.compose.viewmodel.koinViewModel

/**
 * Notes list screen with a staggered grid (Google Keep style).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    onNoteClick: (Long) -> Unit,
    onCreateNote: () -> Unit,
    viewModel: NotesViewModel = koinViewModel(),
) {
    val notes by viewModel.notes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchQuery,
                        onQueryChange = viewModel::onSearchQueryChanged,
                        onSearch = {},
                        expanded = false,
                        onExpandedChange = {},
                        placeholder = { Text("Search notes") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        trailingIcon = {
                            AnimatedVisibility(
                                visible = searchQuery.isNotEmpty(),
                                enter = fadeIn(),
                                exit = fadeOut(),
                            ) {
                                IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        },
                    )
                },
                expanded = false,
                onExpandedChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {}
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateNote,
                icon = { Icon(Icons.Default.Add, contentDescription = "New note") },
                text = { Text("New Note") },
            )
        },
    ) { padding ->
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (searchQuery.isEmpty()) "No notes yet" else "No matching notes",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (searchQuery.isEmpty()) "Tap + to create your first note" else "Try a different search",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(160.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp,
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteCard(
                        note = note,
                        onClick = { onNoteClick(note.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (note.color == NoteColor.DEFAULT) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        note.color.toColor()
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
        ) {
            if (note.isPinned) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Icon(
                        Icons.Outlined.PushPin,
                        contentDescription = "Pinned",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            if (note.title.isNotBlank()) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            if (note.content.isNotBlank()) {
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 8,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            if (note.labels.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    note.labels.take(3).forEach { label ->
                        SuggestionChip(
                            onClick = {},
                            label = {
                                Text(
                                    label,
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}
