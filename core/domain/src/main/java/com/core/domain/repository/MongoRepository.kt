package com.core.domain.repository

import com.core.domain.RequestState
import com.core.domain.models.Journal
import com.core.domain.models.JournalEntry
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

interface MongoRepository {
    fun configureTheRealm()

    suspend fun login(tokenId: String): Boolean
    suspend fun logout(): Boolean
    suspend fun isLoggedIn(): Boolean

    fun getAllDiaries(): Flow<Journal>
    fun getFilteredDiaries(zonedDateTime: ZonedDateTime): Flow<Journal>
    fun getSelectedDiary(diaryId: String): Flow<RequestState<JournalEntry>>
    suspend fun insertDiary(journalEntry: JournalEntry): RequestState<JournalEntry>
    suspend fun updateDiary(journalEntry: JournalEntry): RequestState<JournalEntry>
    suspend fun deleteDiary(id: String): RequestState<Boolean>
    suspend fun deleteAllDiaries(): RequestState<Boolean>
}
