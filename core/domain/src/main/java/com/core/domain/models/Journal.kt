package com.core.domain.models

import com.core.domain.RequestState
import java.time.LocalDate

typealias Journal = RequestState<Map<LocalDate, List<JournalEntry>>>
