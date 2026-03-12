package io.github.starfreck.sanchay.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.*
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onOpenSettings: () -> Unit,
    viewModel: CalendarViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navSuiteType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
    val isMobile = navSuiteType == NavigationSuiteType.NavigationBar

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "${uiState.currentMonth.name.lowercase().replaceFirstChar { it.uppercase() }} ${uiState.currentYear}",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        IconButton(onClick = viewModel::onPreviousMonth) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous Month")
                        }
                        IconButton(onClick = viewModel::onNextMonth) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next Month")
                        }
                    }
                },
                actions = {
                    if (isMobile) {
                        IconButton(onClick = onOpenSettings) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Add Event */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    ) { padding ->
        if (isMobile) {
            CalendarScheduleView(uiState, modifier = Modifier.padding(padding))
        } else {
            CalendarMonthGrid(uiState, onDateSelected = viewModel::onDateSelected, modifier = Modifier.padding(padding))
        }
    }
}

@Composable
fun CalendarMonthGrid(
    uiState: CalendarUiState,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val daysInMonth = getDaysInMonth(uiState.currentYear, uiState.currentMonth)
    val firstDayOfMonth = LocalDate(uiState.currentYear, uiState.currentMonth, 1)
    val dayOfWeekOffset = firstDayOfMonth.dayOfWeek.ordinal // 0-6 (Mon-Sun)
    
    // Adjust for Sunday start if preferred, but let's stick to Monday for now
    val days = (0 until 42).map { index ->
        val dateIndex = index - dayOfWeekOffset
        if (dateIndex in 0 until daysInMonth) {
            LocalDate(uiState.currentYear, uiState.currentMonth, dateIndex + 1)
        } else if (dateIndex < 0) {
            // Placeholder/Previous month days could be added here
            null
        } else {
            // Next month days
            null
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(8.dp)) {
        // Weekday Headers
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(days.size) { index ->
                val date = days[index]
                CalendarDayItem(
                    date = date,
                    isSelected = date == uiState.selectedDate,
                    isToday = date == uiState.today,
                    onClick = { date?.let { onDateSelected(it) } }
                )
            }
        }
    }
}

@Composable
fun CalendarDayItem(
    date: LocalDate?,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else Color.Transparent)
            .clickable(enabled = date != null, onClick = onClick)
            .padding(4.dp)
    ) {
        if (date != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = date.day.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                    modifier = if (isToday) {
                        Modifier
                            .size(24.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                            .wrapContentSize(Alignment.Center)
                    } else Modifier
                )
                // Event indicators could go here
            }
        }
    }
}

@Composable
fun CalendarScheduleView(
    uiState: CalendarUiState,
    modifier: Modifier = Modifier
) {
    // Basic Agenda View
    val days = (1..getDaysInMonth(uiState.currentYear, uiState.currentMonth)).map {
        LocalDate(uiState.currentYear, uiState.currentMonth, it)
    }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(days) { date ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(48.dp)) {
                    Text(
                        date.dayOfWeek.name.take(3),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        date.day.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = if (date == uiState.today) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                // Placeholder for events
                Surface(
                    modifier = Modifier.weight(1f).height(60.dp),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.padding(horizontal = 12.dp)) {
                        Text("No events", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        }
    }
}

fun getDaysInMonth(year: Int, month: Month): Int {
    return when (month) {
        Month.FEBRUARY -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
        else -> 31
    }
}
