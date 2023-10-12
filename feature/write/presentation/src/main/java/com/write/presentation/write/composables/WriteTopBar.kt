package com.write.presentation.write.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.core.ui.models.MoodUI
import com.artemissoftware.util.DateTimeConstants
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.write.presentation.write.WriteState
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WriteTopBar(
    state: WriteState,
    onDateTimeUpdated: (ZonedDateTime) -> Unit,
    onDeleteConfirmed: () -> Unit,
    onBackPressed: () -> Unit,
) {
    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    var currentTime by remember { mutableStateOf(LocalTime.now()) }

    val formattedDate = remember(key1 = currentDate) {
        DateTimeFormatter
            .ofPattern(DateTimeConstants.FORMAT_dd_MM_yyyy)
            .format(currentDate).uppercase()
    }
    val formattedTime = remember(key1 = currentTime) {
        DateTimeFormatter
            .ofPattern(DateTimeConstants.FORMAT_hh_mm_a)
            .format(currentTime).uppercase()
    }

    val dateDialog = rememberSheetState()
    val timeDialog = rememberSheetState()
    var dateTimeUpdated by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Arrow Icon",
                )
            }
        },
        title = {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = state.mood.name,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                    ),
                    textAlign = TextAlign.Center,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = getDateTime(state = state, formattedDate = formattedDate, formattedTime = formattedTime, dateTimeUpdated = dateTimeUpdated),
                    style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize),
                    textAlign = TextAlign.Center,
                )
            }
        },
        actions = {
            if (dateTimeUpdated) {
                IconButton(
                    onClick = {
                        currentDate = LocalDate.now()
                        currentTime = LocalTime.now()
                        dateTimeUpdated = false
                        onDateTimeUpdated(
                            ZonedDateTime.of(
                                currentDate,
                                currentTime,
                                ZoneId.systemDefault(),
                            ),
                        )
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Icon",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                )
            } else {
                IconButton(onClick = { dateDialog.show() }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date Icon",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            state.selectedDiary?.let {
                DeleteDiaryAction(
                    selectedDiary = it,
                    onDeleteConfirmed = onDeleteConfirmed,
                )
            }
        },
    )

    CalendarDialog(
        state = dateDialog,
        selection = CalendarSelection.Date { localDate ->
            currentDate = localDate
            timeDialog.show()
        },
        config = CalendarConfig(monthSelection = true, yearSelection = true),
    )

    ClockDialog(
        state = timeDialog,
        selection = ClockSelection.HoursMinutes { hours, minutes ->
            currentTime = LocalTime.of(hours, minutes)
            dateTimeUpdated = true
            onDateTimeUpdated(
                ZonedDateTime.of(
                    currentDate,
                    currentTime,
                    ZoneId.systemDefault(),
                ),
            )
        },
    )
}

private fun getDateTime(state: WriteState, formattedDate: String, formattedTime: String, dateTimeUpdated: Boolean): String {
    with(state) {
        return if (selectedDiary != null && dateTimeUpdated) {
            "$formattedDate, $formattedTime"
        } else if (selectedDiary != null) {
            getSelectedDiaryDateTime()
        } else {
            "$formattedDate, $formattedTime"
        }
    }
}

@Composable
@Preview
private fun WriteTopBarPreview() {
    WriteTopBar(
        state = WriteState(
            title = "Title one",
            description = "Description one",
            mood = MoodUI.Angry,
        ),
        onBackPressed = {},
        onDeleteConfirmed = {},
        onDateTimeUpdated = {},
    )
}
