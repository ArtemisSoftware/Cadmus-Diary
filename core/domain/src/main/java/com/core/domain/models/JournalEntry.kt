package com.core.domain.models

import java.time.Instant

data class JournalEntry(
    val id: String,
    val ownerId: String,
    val title: String,
    val description: String,
    val date: Instant,
    val mood: String = Mood.Neutral.name,
    val images: List<String> = emptyList(),
)
