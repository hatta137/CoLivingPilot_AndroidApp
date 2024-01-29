package de.fhe.ai.colivingpilot.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import de.fhe.ai.colivingpilot.model.Task
import de.fhe.ai.colivingpilot.storage.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class TaskViewModel()
    : ViewModel() {
    private val repository : Repository = Repository()
    val tasks : LiveData<List<Task>> = repository.getTasks().asLiveData()

    data class NewTask (
        val title: String,
        val notes: String,
        val beerCount: Int
    )

    fun addTask(task : NewTask) {
        viewModelScope.launch(Dispatchers.IO) {

            val newTask = Task(
                UUID.randomUUID().toString(),
                task.title,
                task.notes,
                "",
                task.beerCount)

            repository.addTask(newTask)
        }
    }

    fun deleteTask(item: Int) {
        val task : Task? = tasks.value?.get(item)
        viewModelScope.launch(Dispatchers.IO) {

            if (task != null) {
                repository.deleteTask(task)
            }
        }
    }

}