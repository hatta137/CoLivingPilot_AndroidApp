package de.fhe.ai.colivingpilot.tasks.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import de.fhe.ai.colivingpilot.model.Task
import de.fhe.ai.colivingpilot.storage.Repository
import de.fhe.ai.colivingpilot.tasks.ViewTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskDetailViewModel(id: String)
    : ViewModel() {
    private val repository : Repository = Repository()
    val task : LiveData<Task> = repository.getTask(id).asLiveData()
}