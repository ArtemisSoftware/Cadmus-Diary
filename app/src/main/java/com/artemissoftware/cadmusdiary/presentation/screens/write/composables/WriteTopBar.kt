package com.artemissoftware.cadmusdiary.presentation.screens.write.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.artemissoftware.cadmusdiary.domain.model.Diary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(
    selectedDiary: Diary?,
//    moodName: () -> String,
//    onDateTimeUpdated: (ZonedDateTime) -> Unit,
    onDeleteConfirmed: () -> Unit,
    onBackPressed: () -> Unit,
) {
//    var currentDate by remember { mutableStateOf(LocalDate.now()) }
//    var currentTime by remember { mutableStateOf(LocalTime.now()) }
//    val dateDialog = rememberSheetState()
//    val timeDialog = rememberSheetState()
//    val formattedDate = remember(key1 = currentDate) {
//        DateTimeFormatter
//            .ofPattern("dd MMM yyyy")
//            .format(currentDate).uppercase()
//    }
//    val formattedTime = remember(key1 = currentTime) {
//        DateTimeFormatter
//            .ofPattern("hh:mm a")
//            .format(currentTime).uppercase()
//    }
//    var dateTimeUpdated by remember { mutableStateOf(false) }
//    val selectedDiaryDateTime = remember(selectedDiary) {
//        if (selectedDiary != null) {
//            DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.getDefault())
//                .withZone(ZoneId.systemDefault())
//                .format(selectedDiary.date.toInstant())
//        } else "Unknown"
//    }
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
                    text = /*moodName()*/"Happy",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                    ),
                    textAlign = TextAlign.Center,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = /*if (selectedDiary != null && dateTimeUpdated) "$formattedDate, $formattedTime"
                else if (selectedDiary != null) selectedDiaryDateTime
                else "$formattedDate, $formattedTime"*/ "10 Jan",
                    style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize),
                    textAlign = TextAlign.Center,
                )
            }
        },
        actions = {
//        if (dateTimeUpdated) {
//            IconButton(onClick = {
//                currentDate = LocalDate.now()
//                currentTime = LocalTime.now()
//                dateTimeUpdated = false
//                onDateTimeUpdated(
//                    ZonedDateTime.of(
//                        currentDate,
//                        currentTime,
//                        ZoneId.systemDefault()
//                    )
//                )
//            }) {
//                Icon(
//                    imageVector = Icons.Default.Close,
//                    contentDescription = "Close Icon",
//                    tint = MaterialTheme.colorScheme.onSurface
//                )
//            }
//        } else {
            IconButton(onClick = { /*dateDialog.show()*/ }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date Icon",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
//        }
            if (selectedDiary != null) {
                DeleteDiaryAction(
                    selectedDiary = selectedDiary,
                    onDeleteConfirmed = onDeleteConfirmed,
                )
            }
        },
    )
//
//    CalendarDialog(
//    state = dateDialog,
//    selection = CalendarSelection.Date { localDate ->
//        currentDate = localDate
//        timeDialog.show()
//    },
//    config = CalendarConfig(monthSelection = true, yearSelection = true)
//    )
//
//    ClockDialog(
//    state = timeDialog,
//    selection = ClockSelection.HoursMinutes { hours, minutes ->
//        currentTime = LocalTime.of(hours, minutes)
//        dateTimeUpdated = true
//        onDateTimeUpdated(
//            ZonedDateTime.of(
//                currentDate,
//                currentTime,
//                ZoneId.systemDefault()
//            )
//        )
//    }
//    )
}

@Composable
@Preview
private fun WriteTopBarPreview() {
    WriteTopBar(
        selectedDiary = null,
        onBackPressed = {},
        onDeleteConfirmed = {},
//        state = HomeState(),
    )
}
