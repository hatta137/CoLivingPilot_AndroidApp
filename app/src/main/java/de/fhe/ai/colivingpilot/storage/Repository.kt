package de.fhe.ai.colivingpilot.storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import android.util.Log
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.http.RetrofitClient
import de.fhe.ai.colivingpilot.model.ShoppingListItem
import de.fhe.ai.colivingpilot.model.Task
import de.fhe.ai.colivingpilot.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class Repository {
    private val db: WgDatabase = WgDatabase.getInstance(CoLiPiApplication.applicationContext())
    private val userDao: UserDao = db.userDao()
    private val taskDao: TaskDao = db.taskDao()
    private val shoppingListItemDao: ShoppingListItemDao = db.shoppingListItemDao()
    private val taskAssignedUserDao: TaskAssignedUserDao = db.taskAssignedUserDao()

    suspend fun refresh() = withContext(Dispatchers.IO) {
        val response = RetrofitClient.instance.getWgData().execute()
        if (!response.isSuccessful) {
            Log.e(CoLiPiApplication.LOG_TAG, "Failed to fetch WG data")
            return@withContext
        }

        CoLiPiApplication.instance.keyValueStore.writeString("wg_name", "")
        CoLiPiApplication.instance.keyValueStore.writeString("wg_code", "")
        CoLiPiApplication.instance.keyValueStore.writeInt("wg_max_members", -1)

        userDao.deleteAll() // triggers cascading deletes in all tables
        taskDao.deleteAll() // TODO: removable once Task has reference to User

        val body = response.body()
        body?.let { resp ->
            CoLiPiApplication.instance.keyValueStore.writeString("wg_name", resp.data.wg.name)
            CoLiPiApplication.instance.keyValueStore.writeString("wg_code", resp.data.wg.invitationCode)
            CoLiPiApplication.instance.keyValueStore.writeInt("wg_max_members", resp.data.wg.maximumMembers)
            val creator = resp.data.wg.creator
            resp.data.wg.members.forEach { member ->
                db.userDao().insert(User(member.id, member.username, member.beercounter, creator.username == member.username))
            }
            resp.data.wg.shoppingList.forEach { item ->
                db.shoppingListItemDao().insert(ShoppingListItem(item.id, item.title, item.notes, item.creator.id))
            }
            resp.data.wg.tasks.forEach { task ->
                db.taskDao().insert(Task(task.id, task.title, task.description, task.beerbonus))
            }
        }
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

}