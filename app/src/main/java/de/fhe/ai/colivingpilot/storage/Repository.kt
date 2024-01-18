package de.fhe.ai.colivingpilot.storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.model.ShoppingListItem
import de.fhe.ai.colivingpilot.model.Task
import de.fhe.ai.colivingpilot.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class Repository(

) {
    private val db: WgDatabase = WgDatabase.getInstance(CoLiPiApplication.applicationContext())
    private val userDao: UserDao = db.userDao()
    private val taskDao: TaskDao = db.taskDao()
    private val shoppingListItemDao: ShoppingListItemDao = db.shoppingListItemDao()
    private val taskAssignedUserDao: TaskAssignedUserDao = db.taskAssignedUserDao()

    suspend fun updateAll() = withContext(Dispatchers.IO) {
        userDao.deleteAll() // triggers cascading deletes in all tables
        // TODO: ... get json data from backend and insert everything
    }

    fun getUsersFlow(): Flow<List<User>> {
        return userDao.getUsersFlow()
    }

    fun getTasks(): List<Task> {
        return taskDao.getTasks()
    }

    fun getShoppingListItems(): List<ShoppingListItem> {
        return shoppingListItemDao.getShoppingListItems()
    }

    suspend fun addUser(user: User) {
        userDao.insert(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.delete(user)
    }

    suspend fun getUserById(id: String): User {
        return userDao.getUserById(id)
    }


}