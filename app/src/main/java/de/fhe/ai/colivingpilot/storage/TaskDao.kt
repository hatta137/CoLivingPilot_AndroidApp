package de.fhe.ai.colivingpilot.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import de.fhe.ai.colivingpilot.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    fun insert(vararg task: Task)

    // TODO: removable once Task has reference to User
    @Query("DELETE FROM tasks")
    fun deleteAll()

    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<Task>>

    @Delete
    fun delete(vararg task: Task)

}