package com.write.presentation.write.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.core.domain.models.JournalEntry
import com.core.ui.components.dialog.DisplayAlertDialog
import com.write.presentation.R
import java.time.Instant
import com.core.ui.R as CoreUiR

@Composable
fun DeleteDiaryAction(
    selectedDiary: JournalEntry,
    onDeleteConfirmed: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
    ) {
        DropdownMenuItem(
            text = {
                Text(text = stringResource(id = CoreUiR.string.delete))
            },
            onClick = {
                openDialog = true
                expanded = false
            },
        )
    }
    DisplayAlertDialog(
        title = stringResource(id = CoreUiR.string.delete),
        message = stringResource(
            R.string.are_you_sure_you_want_to_permanently_delete_this_diary_note,
            selectedDiary.title,
        ),
        dialogOpened = openDialog,
        onDialogClosed = { openDialog = false },
        onYesClicked = onDeleteConfirmed,
    )
    IconButton(onClick = { expanded = !expanded }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Overflow Menu Icon",
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
@Preview
private fun DeleteDiaryActionPreview() {
    DeleteDiaryAction(
        selectedDiary = JournalEntry(
            id = "2",
            title = "My Diary",
            ownerId = "23",
            date = Instant.now(),
            description =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        ),
        onDeleteConfirmed = {},
    )
}
