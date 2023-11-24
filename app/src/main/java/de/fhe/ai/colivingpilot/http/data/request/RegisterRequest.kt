package de.fhe.ai.colivingpilot.http.data.request

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)
