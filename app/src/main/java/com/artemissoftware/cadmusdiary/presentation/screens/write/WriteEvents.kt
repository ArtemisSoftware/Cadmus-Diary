package com.artemissoftware.cadmusdiary.presentation.screens.write

sealed class WriteEvents {

    object PopBackStack : WriteEvents()

    data class SetTitle(val title: String) : WriteEvents()

    data class SetDescription(val description: String) : WriteEvents()
}
