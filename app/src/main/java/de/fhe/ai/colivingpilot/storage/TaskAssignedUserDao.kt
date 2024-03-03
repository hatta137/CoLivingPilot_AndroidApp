package de.fhe.ai.colivingpilot.storage

import androidx.room.Dao
import androidx.room.Insert
import de.fhe.ai.colivingpilot.model.TaskAssignedUser

@Dao
interface TaskAssignedUserDao {

    @Insert
    fun insert(vararg taskAssignedUser: TaskAssignedUser)
}
