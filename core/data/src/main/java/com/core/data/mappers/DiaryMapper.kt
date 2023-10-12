package com.core.data.mappers

import com.artemissoftware.util.extensions.toInstant
import com.artemissoftware.util.extensions.toRealmInstant
import com.core.data.realm.models.Diary
import com.core.domain.models.JournalEntry
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import org.mongodb.kbson.ObjectId

fun Diary.toJournal(): JournalEntry {
    return JournalEntry(
        id = _id.toHexString(),
        ownerId = ownerId,
        title = title,
        description = description,
        date = date.toInstant(),
        mood = mood,
        images = images.map { it },
    )
}

fun JournalEntry.toDiary(ownerId: String): Diary {
    val journalTitle = this.title
    val journalMood = this.mood
    val journalDescription = this.description
    val journalDate = this.date.toRealmInstant()
    val journalImages = this.images.toRealmList()

    return Diary().apply { ->
        this._id = ObjectId.invoke(id)
        this.ownerId = ownerId
        this.title = journalTitle
        this.description = journalDescription
        this.date = journalDate
        this.mood = journalMood
        this.images = journalImages as RealmList<String>
    }
}
