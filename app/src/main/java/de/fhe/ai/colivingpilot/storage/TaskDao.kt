package de.fhe.ai.colivingpilot.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import de.fhe.ai.colivingpilot.model.Task

@Dao
interface TaskDao {

    @Insert
    fun insert(vararg task: Task)

    @Query("SELECT * FROM tasks")
    fun getTasks(): List<Task>

}