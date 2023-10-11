package com.core.domain.repository

import com.core.domain.RequestState
import com.core.domain.models.Journal
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.ZonedDateTime

interface MongoRepository {
    fun configureTheRealm()

    suspend fun login(tokenId: String): Boolean
    suspend fun logout(): Boolean
    suspend fun isLoggedIn(): Boolean

    fun getAllDiaries(): Flow<Journal>
    fun getFilteredDiaries(zonedDateTime: ZonedDateTime): Flow<Journal>
//    fun getSelectedDiary(diaryId: ObjectId): Flow<RequestState<Diary>>
//    suspend fun insertDiary(diary: Diary): RequestState<Diary>
//    suspend fun updateDiary(diary: Diary): RequestState<Diary>
//    suspend fun deleteDiary(id: ObjectId): RequestState<Boolean>
//    suspend fun deleteAllDiaries(): RequestState<Boolean>

}
