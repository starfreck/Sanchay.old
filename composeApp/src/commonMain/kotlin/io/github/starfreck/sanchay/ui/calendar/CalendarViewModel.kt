package io.github.starfreck.sanchay.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.*
import kotlin.time.Clock

data class CalendarUiState(
    val currentMonth: Month,
    val currentYear: Int,
    val selectedDate: LocalDate,
    val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
)

class CalendarViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        CalendarUiState(
            currentMonth = Clock.System.todayIn(TimeZone.currentSystemDefault()).month,
            currentYear = Clock.System.todayIn(TimeZone.currentSystemDefault()).year,
            selectedDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        )
    )
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    fun onPreviousMonth() {
        val current = LocalDate(_uiState.value.currentYear, _uiState.value.currentMonth, 1)
        val prev = current.minus(1, DateTimeUnit.MONTH)
        _uiState.value = _uiState.value.copy(
            currentMonth = prev.month,
            currentYear = prev.year
        )
    }

    fun onNextMonth() {
        val current = LocalDate(_uiState.value.currentYear, _uiState.value.currentMonth, 1)
        val next = current.plus(1, DateTimeUnit.MONTH)
        _uiState.value = _uiState.value.copy(
            currentMonth = next.month,
            currentYear = next.year
        )
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
    }
}
