package de.fhe.ai.colivingpilot.wg

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.core.KeyValueStore
import de.fhe.ai.colivingpilot.model.User
import de.fhe.ai.colivingpilot.storage.Repository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.UUID


data class WgFragmentState(
    val isEditMode: Boolean,
) : Serializable

data class UserUiItem(
    val id: String,
    val username: String,
    val beerCount: Int,
    val emoji: String
)
data class UserLongClickDialogState(
    val show : Boolean,
    val userLongClickDialog: UserLongClickDialog?
) : Serializable
data class UserLongClickDialog(
    val selectedEmoji: String,
    val username: String,
    val id: String
) : Serializable

class WgViewmodel(
    val state: SavedStateHandle
) : ViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val repository = Repository()
    private lateinit var keyValueStore: KeyValueStore

    private val _wgName = MutableLiveData<String>()
    val wgName: LiveData<String>
        get() = _wgName

    val userUiItems: LiveData<List<UserUiItem>> = repository.getUsersFlow().map { users ->
        users.map {
            UserUiItem(
                id = it.id,
                username = it.username,
                beerCount = it.beerCounter,
                emoji =
                if (keyValueStore.readString(it.username + "_emoji") == "") {
                    keyValueStore.writeString(it.username + "_emoji", "👦")
                    keyValueStore.readString(it.username + "_emoji")
                } else {
                    keyValueStore.readString(it.username + "_emoji")
                },
            )
        }
    }.asLiveData()

    private val _wgFragmentState = state.getLiveData<WgFragmentState>("wgFragmentState")
    val wgFragmentState: LiveData<WgFragmentState>
        get() = _wgFragmentState

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    init {
        keyValueStore = CoLiPiApplication.instance.keyValueStore
        keyValueStore.registerOnSharedPreferenceChangeListener(this)
        _wgName.value = keyValueStore.readString("wg_name")
        if (_wgName.value == "") {
            _wgName.value = "WG"
            keyValueStore.writeString("wg_name", "WG")
        }
        val wgFragmentState = state.get<WgFragmentState>("wgFragmentState") ?: WgFragmentState(isEditMode = false)
        _wgFragmentState.value = wgFragmentState
    }

    fun onEvent(event: WgEvent) {
        when (event) {
            is WgEvent.OnClickUser -> {
                sendEvent(UiEvent.Navigate("user/${event.username}"))
            }

            is WgEvent.OnLongClickUser -> {
                sendEvent(
                    UiEvent.ShowUserLongClickDialog(
                        event.user.username,
                        event.user.id
                    )
                )
            }

            is WgEvent.OnClickEditWgButton -> {
                val isEditMode = state.get<WgFragmentState>("wgFragmentState")?.isEditMode
                Log.d(CoLiPiApplication.LOG_TAG, "SettingsFragment OnCLickEdit: wgFragmentState.isEditMode = $isEditMode")
                state["wgFragmentState"] = _wgFragmentState.value?.copy(isEditMode = true)

            }

            is WgEvent.OnChangeWgName -> {
                sendEvent(UiEvent.ShowSnackbar(R.string.wg_name_changed))
                sendEvent(UiEvent.deactivateEditMode)
                keyValueStore.writeString("wg_name", event.wgName)
            }

            is WgEvent.OnClickOutsideEditMode -> {
                val isEditMode = state.get<WgFragmentState>("wgFragmentState")?.isEditMode
                Log.d(CoLiPiApplication.LOG_TAG, "SettingsFragment OnClickOutside: wgFragmentState.isEditMode = $isEditMode")
                state["wgFragmentState"] = _wgFragmentState.value?.copy(isEditMode = false)

            }

            is WgEvent.OnDialogOkClick -> {
                keyValueStore.writeString(event.user + "_emoji", event.selectedEmoji)
            }

            is WgEvent.OnDialogCancelClick -> {

            }

            is WgEvent.OnClickAddUserButton -> {
                viewModelScope.launch {
                    val user = User(
                        UUID.randomUUID().toString(),
                        "Hendrik",
                        1337,
                        false
                    )
                    Log.i(CoLiPiApplication.LOG_TAG, "User created")
                    repository.addUser(user)
                }
                Log.i(CoLiPiApplication.LOG_TAG, "User added")
            }

            is WgEvent.OnDeleteUserClick -> {
                viewModelScope.launch {
                    val user = repository.getUserById(event.id)
                    repository.deleteUser(user)
                }
            }
            is WgEvent.OnSettingsClick -> {
                sendEvent(UiEvent.Navigate("settings"))
            }
            is WgEvent.OnClickAddUser -> {
                sendEvent(UiEvent.Navigate("addUser"))
            }
        }
    }

    fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        key?.let {
            if (key == "wg_name") {
                sharedPreferences?.let {
                    val wgName = it.getString(key, "")!!
                    _wgName.value = wgName
                }
            }
            if (key.contains("_emoji")) {
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