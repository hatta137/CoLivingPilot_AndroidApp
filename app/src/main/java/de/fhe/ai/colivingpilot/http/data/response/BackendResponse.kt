package de.fhe.ai.colivingpilot.http.data.response

/**
 * Template for responses coming from our backend
 */
data class BackendResponse<T>(
    val status: String,
    val data: T
)