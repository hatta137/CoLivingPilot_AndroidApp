package de.fhe.ai.colivingpilot.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import de.fhe.ai.colivingpilot.model.User

@Dao
interface UserDao {

    @Insert
    fun insert(vararg user: User)

    @Query("DELETE FROM users")
    fun deleteAll()

    @Query("SELECT * from users")
    fun getUsers(): List<User>

}