package io.github.starfreck.sanchay.ui.tasks.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import io.github.starfreck.sanchay.domain.model.TaskStep

@Composable
fun TaskStepItem(
    step: TaskStep,
    onToggle: () -> Unit,
    onRemove: () -> Unit,
    onRename: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onToggle) {
            Icon(
                imageVector = if (step.isCompleted) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                contentDescription = "Toggle Step",
                tint = if (step.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
            )
        }

        TextField(
            value = step.title,
            onValueChange = onRename,
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                disabledContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                textDecoration = if (step.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            ),
            singleLine = true
        )

        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove Step",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}
