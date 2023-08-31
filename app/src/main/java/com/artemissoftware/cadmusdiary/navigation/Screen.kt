package com.artemissoftware.cadmusdiary.navigation

sealed class Screen(val route: String) {
    object Authentication : Screen(route = "authentication")
    object Home : Screen(route = "home")
    object Write : Screen(
        route = "write?$WRITE_SCREEN_ARGUMENT_KEY=" +
            "{$WRITE_SCREEN_ARGUMENT_KEY}",
    ) {
        fun passDiaryId(diaryId: String) =
            "write?$WRITE_SCREEN_ARGUMENT_KEY=$diaryId"
    }

    companion object {
        const val WRITE_SCREEN_ARGUMENT_KEY = "diaryId"
    }
}
