package de.fhe.ai.colivingpilot.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import de.fhe.ai.colivingpilot.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun insert(vararg user: User)

    @Query("DELETE FROM users")
    fun deleteAll()

    @Query("SELECT * FROM users ORDER by beer_counter DESC")
    fun getUsersFlow(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE username = 'Hendrik'")
    fun getTestUser(): User
}