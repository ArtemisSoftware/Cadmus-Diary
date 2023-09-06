package com.artemissoftware.cadmusdiary.presentation.screens.home.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.artemissoftware.cadmusdiary.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
//    scrollBehavior: TopAppBarScrollBehavior,
    onMenuClicked: () -> Unit,
//    dateIsSelected: Boolean,
//    onDateSelected: (ZonedDateTime) -> Unit,
//    onDateReset: () -> Unit,
) {
//    val dateDialog = rememberSheetState()
    TopAppBar(
//        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = onMenuClicked) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Hamburger Menu Icon",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.diary),
            )
        },
        actions = {
//            if (dateIsSelected) {
//                IconButton(onClick = onDateReset) {
//                    Icon(
//                        imageVector = Icons.Default.Close,
//                        contentDescription = "Close Icon",
//                        tint = MaterialTheme.colorScheme.onSurface
//                    )
//                }
//            } else {
            IconButton(onClick = onMenuClicked /*{ dateDialog.show() }*/) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date Icon",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
//            }
        },
    )

//    CalendarDialog(
//        state = dateDialog,
//        selection = CalendarSelection.Date { localDate ->
//            onDateSelected(
//                ZonedDateTime.of(
//                    localDate,
//                    LocalTime.now(),
//                    ZoneId.systemDefault()
//                )
//            )
//        },
//        config = CalendarConfig(monthSelection = true, yearSelection = true)
//    )
}

@Composable
@Preview
private fun HomeTopBarPreview() {
    HomeTopBar(
        onMenuClicked = {},
    )
}