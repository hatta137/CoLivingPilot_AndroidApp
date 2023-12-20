package de.fhe.ai.colivingpilot.settings

sealed class WgEvent{
    data class OnClickUser(val username: String) : WgEvent()
    data class OnLongClickUser(val username: String) : WgEvent()
    data class OnChangeWgName(val wgName: String) : WgEvent()
    object OnClickEditWgButton : WgEvent()
    object OnClickAddUserButton : WgEvent()
    object OnClickOutsideEditMode : WgEvent()

    data class OnDialogOkClick (val user: String, val selectedEmoji : String) : WgEvent()
    object OnDialogCancelClick : WgEvent()


}
