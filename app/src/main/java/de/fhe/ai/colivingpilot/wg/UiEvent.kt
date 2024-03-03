package de.fhe.ai.colivingpilot.wg

sealed class UiEvent {

    object PopBackStack : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class ShowSnackbar(
        val message: Int,
        val action: String? = null,
    ) : UiEvent()

    data class ShowUserLongClickDialog(
        val username: String,
        val id: String,
    ) : UiEvent()

    data class updateEmoji(
        val username: String, val emoji: String
    ) : UiEvent()

    data class updateWgName(
        val wgName: String
    ) : UiEvent()

    object activateEditMode : UiEvent()

    object deactivateEditMode : UiEvent()
}