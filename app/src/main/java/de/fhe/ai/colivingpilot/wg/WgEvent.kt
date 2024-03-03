package de.fhe.ai.colivingpilot.wg

sealed class WgEvent{
    data class OnClickUser(val username: String) : WgEvent()
    data class OnLongClickUser(val user: UserUiItem) : WgEvent()
    data class OnChangeWgName(val wgName: String) : WgEvent()
    object OnClickEditWgButton : WgEvent()
    object OnClickAddUserButton : WgEvent()
    object OnClickOutsideEditMode : WgEvent()
    data class OnDialogOkClick (val user: String, val selectedEmoji : String) : WgEvent()
    object OnDialogCancelClick : WgEvent()
    object OnSettingsClick : WgEvent()
    object OnClickAddUser : WgEvent()
}
