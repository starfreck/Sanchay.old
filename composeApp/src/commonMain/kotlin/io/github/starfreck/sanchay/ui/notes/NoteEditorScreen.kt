package io.github.starfreck.sanchay.ui.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.starfreck.sanchay.domain.model.NoteColor
import io.github.starfreck.sanchay.theme.toColor
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Note editor screen for creating and editing notes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    noteId: Long,
    onBack: () -> Unit,
    viewModel: NoteEditorViewModel = koinViewModel { parametersOf(noteId) },
) {
    val note by viewModel.note.collectAsState()
    val isSaved by viewModel.isSaved.collectAsState()

    // Navigate back after save
    LaunchedEffect(isSaved) {
        if (isSaved) onBack()
    }

    val backgroundColor = if (note.color == NoteColor.DEFAULT) {
        MaterialTheme.colorScheme.surface
    } else {
        note.color.toColor()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::onPinnedToggled) {
                        Icon(
                            Icons.Outlined.PushPin,
                            contentDescription = if (note.isPinned) "Unpin" else "Pin",
                            tint = if (note.isPinned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = viewModel::saveNote) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor,
                ),
            )
        },
        containerColor = backgroundColor,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            // Title field
            OutlinedTextField(
                value = note.title,
                onValueChange = viewModel::onTitleChanged,
                placeholder = { Text("Title", style = MaterialTheme.typography.headlineMedium) },
                textStyle = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
                singleLine = true,
            )

            // Content field
            OutlinedTextField(
                value = note.content,
                onValueChange = viewModel::onContentChanged,
                placeholder = { Text("Note") },
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 200.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Color picker
            Text(
                "Color",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                NoteColor.entries.forEach { color ->
                    val displayColor = if (color == NoteColor.DEFAULT) {
                        MaterialTheme.colorScheme.surfaceVariant
                    } else {
                        color.toColor()
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(displayColor)
                            .then(
                                if (note.color == color) {
                                    Modifier.border(
                                        2.dp,
                                        MaterialTheme.colorScheme.primary,
                                        CircleShape,
                                    )
                                } else {
                                    Modifier.border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                        CircleShape,
                                    )
                                }
                            )
                            .clickable { viewModel.onColorChanged(color) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
