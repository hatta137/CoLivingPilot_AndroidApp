package de.fhe.ai.colivingpilot.wg.modals.userLongClick

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.storage.Repository
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
    fun onDeleteUserClick(id: String){
        viewModelScope.launch {
            val user = repository.getUserById(id)
            repository.deleteUser(user)
            Log.d("UserLongClickViewmodel", "User deleted: " + user.username)
        }
    }

    fun onAcceptDialogClick(){

    }
}