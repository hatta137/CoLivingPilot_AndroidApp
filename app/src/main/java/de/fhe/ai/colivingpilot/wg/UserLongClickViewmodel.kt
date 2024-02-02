package de.fhe.ai.colivingpilot.wg

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.storage.Repository
import kotlinx.coroutines.launch

class UserLongClickViewmodel : ViewModel(){
    private val repository = Repository()

    var username: String = ""
    var id: String = ""
    var selectedEmoji: String = ""

    fun onDialogOkClick(emoji: String){
        val sharedPrefs = CoLiPiApplication.instance.keyValueStore
        sharedPrefs.writeString(username + "_emoji", emoji)
    }
    fun onDialogCancelClick(){
        // do nothing
    }
    fun onDeleteUserClick(){
        viewModelScope.launch {
            val user = repository.getUserById(id)
            repository.deleteUser(user)
        }
    }
}