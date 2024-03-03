package de.fhe.ai.colivingpilot.wg.modals.userLongClick

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.network.NetworkResultNoData
import de.fhe.ai.colivingpilot.storage.Repository
import de.fhe.ai.colivingpilot.util.UiUtils
import kotlinx.coroutines.launch

class UserLongClickViewmodel : ViewModel(){
    private val repository = Repository()

//    var username: String = ""
//    var id: String = ""
    var selectedEmoji: String = ""

    fun onDialogOkClick(emoji: String, username: String){
        val sharedPrefs = CoLiPiApplication.instance.keyValueStore
        sharedPrefs.writeString(username + "_emoji", emoji)
    }
    fun onDialogCancelClick(){
        // do nothing
    }
    fun onDeleteUserClick(username: String){
        viewModelScope.launch {
            repository.kickUser(username, object : NetworkResultNoData {
                override fun onSuccess() {
                }

                override fun onFailure(code: String?) {
                }
            })
        }
    }

    fun onAcceptDialogClick(){

    }
}