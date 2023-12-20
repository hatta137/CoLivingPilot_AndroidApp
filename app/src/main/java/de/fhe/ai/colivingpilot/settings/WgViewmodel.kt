package de.fhe.ai.colivingpilot.settings

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.core.KeyValueStore
import de.fhe.ai.colivingpilot.storage.Repository
import kotlinx.coroutines.flow.map


data class UserUiItemState(
    val username: String,
    val beerCount: Int,
    val emoji: String
)
class WgViewmodel : ViewModel(), SharedPreferences.OnSharedPreferenceChangeListener

{
    private val repository = Repository()
    private lateinit var keyValueStore: KeyValueStore

    private val _wgName = MutableLiveData<String>()
    val wgName: LiveData<String>
        get() = _wgName

    val userUiItems : LiveData<List<UserUiItemState>> = repository.getUsersFlow().map { users ->
        users.map {
            UserUiItemState(
                username = it.username,
                beerCount = it.beerCounter,
                emoji =
                if(keyValueStore.readString(it.username + "_emoji") == ""){
                    keyValueStore.writeString(it.username + "_emoji", "👦")
                    keyValueStore.readString(it.username + "_emoji")
                } else {
                    keyValueStore.readString(it.username + "_emoji")
                }
            )
        }
    }.asLiveData()


    private val _uiEvent = MutableLiveData<UiEvent>()
    val uiEvent: LiveData<UiEvent>
        get() = _uiEvent

    init {
        keyValueStore = CoLiPiApplication.instance.keyValueStore
        keyValueStore.registerOnSharedPreferenceChangeListener(this)
        _wgName.value = keyValueStore.readString("wg_name")
        if(_wgName.value == ""){
            _wgName.value = "WG"
            keyValueStore.writeString("wg_name", "WG")
        }
    }
    fun onEvent(event: WgEvent){
        when(event){
            is WgEvent.OnClickUser -> {
                sendEvent(UiEvent.Navigate("user/${event.username}"))
            }
            is WgEvent.OnLongClickUser -> {
                sendEvent(UiEvent.ShowUserLongClickDialog(
                    event.username))
            }
            is WgEvent.OnClickAddUserButton -> {

            }
            is WgEvent.OnClickEditWgButton -> {
                sendEvent(UiEvent.activateEditMode)
            }
            is WgEvent.OnChangeWgName -> {
                sendEvent(UiEvent.ShowSnackbar(R.string.wg_name_changed))
                sendEvent(UiEvent.deactivateEditMode)
                keyValueStore.writeString("wg_name", event.wgName)
            }
            is WgEvent.OnClickOutsideEditMode -> {
                sendEvent(UiEvent.deactivateEditMode)
            }
            is WgEvent.OnDialogOkClick -> {
                keyValueStore.writeString(event.user + "_emoji", event.selectedEmoji)
            }
            is WgEvent.OnDialogCancelClick -> {

            }
        }
    }

    fun sendEvent(event: UiEvent){
        _uiEvent.value = event
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        key?.let {
            Log.d(CoLiPiApplication.LOG_TAG, "Shared preferences changed: $key")
            if(key == "wg_name"){
                sharedPreferences?.let {
                    val wgName = it.getString(key, "")!!
                    Log.d(CoLiPiApplication.LOG_TAG, "wg_name changed to" + keyValueStore.readString("wg_name"))
                    _wgName.value = wgName
                }
            }
            if(key.contains("_emoji")){
                sharedPreferences?.let {
                    val emoji = it.getString(key, "")!!
                    sendEvent(UiEvent.updateEmoji(emoji, key.replace("_emoji", "")))
                }
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        keyValueStore.unregisterOnSharedPreferenceChangeListener(this)
    }
}