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
import org.json.JSONObject
import java.lang.Exception


class Repository(
    context: Context
) {

    private val db: WgDatabase = WgDatabase.getDatabase(context)
    private val userDao: UserDao = db.userDao()
    private val taskDao: TaskDao = db.taskDao()
    private val shoppingListItemDao: ShoppingListItemDao = db.shoppingListItemDao()
    private val taskAssignedUserDao: TaskAssignedUserDao = db.taskAssignedUserDao()

    /**
     * Refreshes the app's data by fetching the relevant dataset from the server.
     *
     * @return A pair indicating the success or failure of the operation and an optional error message.
     */
    suspend fun refresh(): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.instance.getWgData().execute()
            if (!response.isSuccessful) {
                val body = response.errorBody()
                body?.let {
                    val json = JSONObject(body.string())
                    return@withContext Pair(false, json.getString("status"))
                }
                return@withContext Pair(false, "")
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
            return@withContext Pair(true, "")
        } catch (_: Exception) {
            Log.e(CoLiPiApplication.LOG_TAG, "Failed to fetch WG data")
            return@withContext Pair(false, "")
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