package de.fhe.ai.colivingpilot.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import de.fhe.ai.colivingpilot.model.Task
import de.fhe.ai.colivingpilot.network.NetworkResult
import de.fhe.ai.colivingpilot.network.NetworkResultNoData
import de.fhe.ai.colivingpilot.storage.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class TaskViewModel()
    : ViewModel() {
    private val repository : Repository = Repository()
    val tasks : LiveData<List<Task>> = repository.getTasks().asLiveData()

    //TODO Auslagern in config fragment
    fun addTask(title: String, notes: String, beerCount: Int, callback: NetworkResult<String>) {
        repository.addTask(title, notes, beerCount, callback)
    }

    fun updateTask(id: String, title: String, notes: String, beerCount: Int, callback: NetworkResultNoData) {
        repository.updateTask(id, title, notes, beerCount, callback)
    }

    fun deleteTask(id: String, callback: NetworkResultNoData) {
        repository.deleteTaskById(id, callback)
    }

}