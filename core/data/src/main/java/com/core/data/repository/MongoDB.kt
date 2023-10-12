package com.core.data.repository

import com.artemissoftware.util.extensions.toRealmInstant
import com.core.data.ConstantsId.APP_ID
import com.core.data.exceptions.UserNotAuthenticatedException
import com.core.data.mappers.toDiary
import com.core.data.mappers.toJournal
import com.core.data.realm.models.Diary
import com.core.domain.RequestState
import com.core.domain.models.Journal
import com.core.domain.models.JournalEntry
import com.core.domain.repository.MongoRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

object MongoDB : MongoRepository {
    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm

    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        if (user != null) {
            val config = SyncConfiguration.Builder(user, setOf(Diary::class))
                .initialSubscriptions { sub ->
                    add(
                        query = sub.query<Diary>(query = "ownerId == $0", user.identity),
                        name = "User's Diaries",
                    )
                }
                .log(LogLevel.ALL) // TODO: verificar isto
                .build()
            realm = Realm.open(config)
        }
    }

    override suspend fun login(tokenId: String): Boolean {
        return app.login(
            Credentials.jwt(tokenId),
            // Credentials.google(tokenId, GoogleAuthType.ID_TOKEN), // needs fix from google to be able to see name, email, picture.....
        ).loggedIn
    }

    override suspend fun logout(): Boolean {
        val user = app.currentUser // TODO: irrelevante?
        return if (user != null) {
            user.logOut()
            true
        } else {
            false
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        val user = app.currentUser
        return (user != null) && user.loggedIn
    }

    override fun getAllDiaries(): Flow<Journal> {
        return if (user != null) {
            try {
                realm.query<Diary>(query = "ownerId == $0", user.identity)
                    .sort(property = "date", sortOrder = Sort.DESCENDING)
                    .asFlow()
                    .map { result ->
                        RequestState.Success(
                            data = result
                                .list
                                .map { it.toJournal() }
                                .groupBy {
                                    it.date
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                },
//                            data = result.list.groupBy {
//                                it.date.toInstant()
//                                    .atZone(ZoneId.systemDefault())
//                                    .toLocalDate()
//                            },
                        )
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override fun getFilteredDiaries(zonedDateTime: ZonedDateTime): Flow<Journal> {
        return if (user != null) {
            try {
                realm.query<Diary>(
                    "ownerId == $0 AND date < $1 AND date > $2",
                    user.id,
                    RealmInstant.from(
                        LocalDateTime.of(
                            zonedDateTime.toLocalDate().plusDays(1),
                            LocalTime.MIDNIGHT,
                        ).toEpochSecond(zonedDateTime.offset),
                        0,
                    ),
                    RealmInstant.from(
                        LocalDateTime.of(
                            zonedDateTime.toLocalDate(),
                            LocalTime.MIDNIGHT,
                        ).toEpochSecond(zonedDateTime.offset),
                        0,
                    ),
                ).asFlow().map { result ->
                    RequestState.Success(
                        data = result
                            .list
                            .map { it.toJournal() }
                            .groupBy {
                                it.date
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            },
                    )
                }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override fun getSelectedDiary(diaryId: String): Flow<RequestState<JournalEntry>> {
        val id = ObjectId.invoke(diaryId)

        return if (user != null) {
            try {
                realm.query<Diary>(query = "_id == $0", id).asFlow().map {
                    RequestState.Success(data = it.list.first().toJournal())
                }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override suspend fun insertDiary(journalEntry: JournalEntry): RequestState<JournalEntry> {
        return if (user != null) {
            realm.write {
                try {
                    val addedDiary = copyToRealm(journalEntry.toDiary(ownerId = user.id))
                    RequestState.Success(data = addedDiary.toJournal())
                } catch (e: Exception) {
                    RequestState.Error(e)
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun updateDiary(journalEntry: JournalEntry): RequestState<JournalEntry> {
        return if (user != null) {
            val id = ObjectId.invoke(journalEntry.id)

            realm.write {
                val queriedDiary = query<Diary>(query = "_id == $0", id).first().find()
                if (queriedDiary != null) {
                    with(journalEntry) {
                        queriedDiary.title = title
                        queriedDiary.description = description
                        queriedDiary.mood = mood
                        queriedDiary.images = images.toRealmList()
                        queriedDiary.date = date.toRealmInstant()
                    }
                    RequestState.Success(data = queriedDiary.toJournal())
                } else {
                    RequestState.Error(error = Exception("Queried Diary does not exist."))
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun deleteDiary(diaryId: String): RequestState<Boolean> {
        return if (user != null) {
            val id = ObjectId.invoke(diaryId)

            realm.write {
                val diary =
                    query<Diary>(query = "_id == $0 AND ownerId == $1", id, user.id)
                        .first().find()

                if (diary != null) {
                    try {
                        delete(diary)
                        RequestState.Success(data = true)
                    } catch (e: Exception) {
                        RequestState.Error(e)
                    }
                } else {
                    RequestState.Error(Exception("Diary does not exist."))
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun deleteAllDiaries(): RequestState<Boolean> {
        return if (user != null) {
            realm.write {
                val diaries = this.query<Diary>("ownerId == $0", user.id).find()
                try {
                    delete(diaries)
                    RequestState.Success(data = true)
                } catch (e: Exception) {
                    RequestState.Error(e)
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }
}
