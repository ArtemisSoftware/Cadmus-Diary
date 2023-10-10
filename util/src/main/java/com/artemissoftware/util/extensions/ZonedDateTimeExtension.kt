package com.artemissoftware.util.extensions

import io.realm.kotlin.types.RealmInstant
import java.time.ZonedDateTime

fun ZonedDateTime.toRealmInstant(): RealmInstant{
    return toInstant().toRealmInstant()
}