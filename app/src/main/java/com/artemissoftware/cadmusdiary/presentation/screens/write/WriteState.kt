package com.artemissoftware.cadmusdiary.presentation.screens.write

import com.artemissoftware.cadmusdiary.domain.model.Mood

data class WriteState(
    val selectedDiaryId: String? = null,
//    val selectedDiary: Diary? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Neutral,
//    val updatedDateTime: RealmInstant? = null
)
