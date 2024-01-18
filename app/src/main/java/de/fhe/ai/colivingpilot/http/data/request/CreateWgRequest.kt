package de.fhe.ai.colivingpilot.http.data.request

data class CreateWgRequest(
    val name: String,
    val maximumMembers: Int
)