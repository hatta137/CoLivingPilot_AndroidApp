package de.fhe.ai.colivingpilot.storage

import android.content.Context
import android.util.Log
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.http.RetrofitClient
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

    suspend fun refresh() = withContext(Dispatchers.IO) {
        val response = RetrofitClient.instance.getWgData().execute()
        if (!response.isSuccessful) {
            Log.e(CoLiPiApplication.LOG_TAG, "Failed to fetch WG data")
            return@withContext
        }

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