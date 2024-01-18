package de.fhe.ai.colivingpilot.settings

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.core.KeyValueStore
import de.fhe.ai.colivingpilot.model.User
import de.fhe.ai.colivingpilot.storage.Repository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID


data class WgFragmentState(
    val isEditMode: Boolean = false,

    )

data class UserUiItem(
    val id: String,
    val username: String,
    val beerCount: Int,
    val emoji: String
)

class WgViewmodel : ViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val repository = Repository()
    private lateinit var keyValueStore: KeyValueStore
    private lateinit var model: GenerativeModel

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

    private val _wgFragmentState = MutableLiveData<WgFragmentState>(
        WgFragmentState(isEditMode = false)
    )
    val wgFragmentState: LiveData<WgFragmentState>
        get() = _wgFragmentState

    private val _uiEvent = MutableLiveData<UiEvent>()
    val uiEvent: LiveData<UiEvent>
        get() = _uiEvent

    init {
        keyValueStore = CoLiPiApplication.instance.getKeyValueStore()
        keyValueStore.registerOnSharedPreferenceChangeListener(this)
        _wgName.value = keyValueStore.readString("wg_name")
        if (_wgName.value == "") {
            _wgName.value = "WG"
            keyValueStore.writeString("wg_name", "WG")
        }
        model = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = "AIzaSyDsWJwVFRINDNzVDcg5eg_2W9sOwVBGID0"
        )
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
                _wgFragmentState.value = _wgFragmentState.value?.copy(
                    isEditMode = true
                )
            }

            is WgEvent.OnChangeWgName -> {
                sendEvent(UiEvent.ShowSnackbar(R.string.wg_name_changed))
                sendEvent(UiEvent.deactivateEditMode)
                keyValueStore.writeString("wg_name", event.wgName)
            }

            is WgEvent.OnClickOutsideEditMode -> {
                _wgFragmentState.value = _wgFragmentState.value?.copy(
                    isEditMode = false
                )
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
                        1337
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

            is WgEvent.OnSuggestWgNameClick -> {
                viewModelScope.launch {
                    val response = try {
                        model.generateContent(
                            content {
                                text("Gib mir einen json array lustiger Namen für eine Wohngemeinschaft")
                            }
                        )
                    } catch (e: Exception) {
                        Log.e(CoLiPiApplication.LOG_TAG, "Error: ${e.message}")
                        null
                    }
                    response?.let {
                        try {
                            // Convert JSON array to List<String> using Gson
                            val gson = Gson()
                            val type = object : TypeToken<List<String>>() {}.type
                            val wgNames: List<String> = gson.fromJson(it.text.toString(), type)

                            // Now you have the list of strings (wgNames)
                            wgNames.forEach { wgName ->
                                Log.d(CoLiPiApplication.LOG_TAG, "wgName: $wgName")
                            }
                        } catch (e: Exception) {
                            Log.e(
                                CoLiPiApplication.LOG_TAG,
                                "Error parsing JSON array: ${e.message}"
                            )
                        }
                    }
                }
            }
        }
    }

    fun sendEvent(event: UiEvent) {
        _uiEvent.value = event
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