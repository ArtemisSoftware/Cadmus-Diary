package com.write.presentation.write.navigation

internal sealed class Screen(val route: String) {
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
