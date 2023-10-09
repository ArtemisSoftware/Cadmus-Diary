package com.core.domain.repository

import com.core.domain.RequestState
import com.core.domain.models.Diary
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.ZonedDateTime

typealias Diaries = RequestState<Map<LocalDate, List<Diary>>>

interface MongoRepository {
    fun configureTheRealm()
    fun getAllDiaries(): Flow<Diaries>
    fun getFilteredDiaries(zonedDateTime: ZonedDateTime): Flow<Diaries>
    fun getSelectedDiary(diaryId: ObjectId): Flow<com.core.domain.RequestState<Diary>>
    suspend fun insertDiary(diary: Diary): com.core.domain.RequestState<Diary>
    suspend fun updateDiary(diary: Diary): com.core.domain.RequestState<Diary>
    suspend fun deleteDiary(id: ObjectId): com.core.domain.RequestState<Boolean>
    suspend fun deleteAllDiaries(): com.core.domain.RequestState<Boolean>
    suspend fun login(tokenId: String): Boolean
}
