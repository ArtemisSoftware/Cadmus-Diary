package com.core.data.mappers

import com.artemissoftware.util.extensions.toInstant
import com.core.data.realm.models.Diary
import com.core.domain.models.JournalEntry

fun Diary.toJournal(): JournalEntry {
    return JournalEntry(
        id = _id.toString(),
        ownerId = ownerId,
        title = title,
        description = description,
        date = date.toInstant(),
        mood = mood,
        images = images.map { it },
    )
}
