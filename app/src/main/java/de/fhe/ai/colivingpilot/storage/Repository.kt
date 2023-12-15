package de.fhe.ai.colivingpilot.storage

import android.content.Context
import de.fhe.ai.colivingpilot.model.ShoppingListItem
import de.fhe.ai.colivingpilot.model.Task
import de.fhe.ai.colivingpilot.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Repository(
    context: Context
) {

    private val db: WgDatabase = WgDatabase.getDatabase(context)
    private val userDao: UserDao = db.userDao()
    private val taskDao: TaskDao = db.taskDao()
    private val shoppingListItemDao: ShoppingListItemDao = db.shoppingListItemDao()
    private val taskAssignedUserDao: TaskAssignedUserDao = db.taskAssignedUserDao()

    suspend fun updateAll() = withContext(Dispatchers.IO) {
        userDao.deleteAll() // triggers cascading deletes in all tables
        // TODO: ... get json data from backend and insert everything
    }

    fun getUsers(): List<User> {
        return userDao.getUsers()
    }

    fun getTasks(): List<Task> {
        return taskDao.getTasks()
    }

    fun getShoppingListItems(): List<ShoppingListItem> {
        return shoppingListItemDao.getShoppingListItems()
    }

}