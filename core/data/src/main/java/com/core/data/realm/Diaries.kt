package com.core.data.realm

import com.core.data.realm.models.Diary
import com.core.domain.RequestState
import java.time.LocalDate

typealias Diaries = RequestState<Map<LocalDate, List<Diary>>>
