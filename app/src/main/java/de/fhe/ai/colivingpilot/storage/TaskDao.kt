package de.fhe.ai.colivingpilot.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import de.fhe.ai.colivingpilot.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Upsert
    fun upsert(vararg task: Task)

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTask(vararg id: String) : Flow<Task>

    // TODO: removable once Task has reference to User
    @Query("DELETE FROM tasks")
    fun deleteAll()

    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<Task>>

    @Delete
    fun delete(vararg task: Task)

    @Query("DELETE FROM tasks WHERE id = :id")
    fun deleteByID(vararg id: String)

}