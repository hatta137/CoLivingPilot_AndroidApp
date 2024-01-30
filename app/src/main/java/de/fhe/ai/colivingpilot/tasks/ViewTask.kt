package de.fhe.ai.colivingpilot.tasks

import java.util.UUID

data class ViewTask (
    val title: String,
    val notes: String,
    val beerCount: Int,
    val id: String = UUID.randomUUID().toString()
)
