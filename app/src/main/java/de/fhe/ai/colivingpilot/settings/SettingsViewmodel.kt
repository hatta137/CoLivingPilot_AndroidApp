package de.fhe.ai.colivingpilot.settings

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.core.KeyValueStore
import de.fhe.ai.colivingpilot.storage.Repository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewmodel @Inject constructor(
    private val repository: Repository
)
    : ViewModel()
{



    private lateinit var keyValueStore: KeyValueStore

    private val _editMode = MutableLiveData<Boolean>()
    val editMode: LiveData<Boolean>
        get() = _editMode

    fun toggleEditMode(){
        _editMode.value = !_editMode.value!!
    }

    val users = repository.getUsersFlow()

    init {
        _editMode.value = false
        Log.d(CoLiPiApplication.LOG_TAG, "SettingsViewmodel created")
        keyValueStore = CoLiPiApplication.instance.getKeyValueStore()

        viewModelScope.launch {
            users.collect{users ->
                users.forEach{user ->
                    val emoji = keyValueStore.readString(user.username + "_emoji")
                    Log.d(CoLiPiApplication.LOG_TAG, "Emoji for ${user.username} is " + emoji)
                    if(emoji == ""){
                        keyValueStore.writeString(user.username + "_emoji", "👦")
                        Log.d(CoLiPiApplication.LOG_TAG, "(flow collect): Emoji for ${user.username} set to " + keyValueStore.readString(user.username.toString() + "_emoji"))
                    }
                }

            }

        }
    }
    fun saveEmoji(username: String, emoji: String){
        keyValueStore.writeString(username + "_emoji", emoji)
    }

    fun updateWgName(wgName: String){
        keyValueStore.writeString("wg_name", wgName)
        //todo update wg name in database
    }


}