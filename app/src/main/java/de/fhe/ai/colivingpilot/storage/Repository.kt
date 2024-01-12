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

    fun getShoppingListItemsFlow(): Flow<List<ShoppingListItem>> {
        return shoppingListItemDao.getShoppingListItemsFlow()
    }

    fun deleteItemFromShoppingList(shoppingListItem: ShoppingListItem){
        shoppingListItemDao.deleteItemFromShoppingList(shoppingListItem)
    }

    fun insertShoppingListItem(shoppingListItem: ShoppingListItem){
        shoppingListItemDao.insert(shoppingListItem)
    }

    fun updateItem(shoppingListItem: ShoppingListItem, boolean: Boolean){
        var updatedItem = shoppingListItem.copy(isChecked = boolean)
        shoppingListItemDao.updateItem(updatedItem)
    }

    //TODO später kommt der User aus der Anmeldung
    fun getTestUser(): User {
        return userDao.getTestUser()
    }
}