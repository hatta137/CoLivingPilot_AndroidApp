package de.fhe.ai.colivingpilot.storage

import android.util.Log
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.network.RetrofitClient
import de.fhe.ai.colivingpilot.model.ShoppingListItem
import de.fhe.ai.colivingpilot.model.Task
import de.fhe.ai.colivingpilot.model.User
import de.fhe.ai.colivingpilot.network.NetworkResult
import de.fhe.ai.colivingpilot.network.NetworkResultNoData
import de.fhe.ai.colivingpilot.network.data.request.AddShoppingListItemRequest
import de.fhe.ai.colivingpilot.network.data.request.AddTaskRequest
import de.fhe.ai.colivingpilot.network.data.request.CheckShoppingListItemRequest
import de.fhe.ai.colivingpilot.network.data.request.RenameWgRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception


class Repository(

) {
    private val db: WgDatabase = WgDatabase.getInstance(CoLiPiApplication.applicationContext())
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
                    db.shoppingListItemDao().insert(ShoppingListItem(item.id, item.title, item.notes, item.creator.id, item.isChecked))
                }
                resp.data.wg.tasks.forEach { task ->
                    db.taskDao().upsert(Task(task.id, task.title, task.description, task.beerbonus))
                }
            }
            return@withContext Pair(true, "")
        } catch (_: Exception) {
            Log.e(CoLiPiApplication.LOG_TAG, "Failed to fetch WG data")
            return@withContext Pair(false, "")
        }
    }

    fun getUsersFlow(): Flow<List<User>> {
        return userDao.getUsersFlow()
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

    fun renameWg(name: String, callback: NetworkResultNoData) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.renameWg(RenameWgRequest(name)).execute()
                if (!response.isSuccessful) {
                    withContext(Dispatchers.Main) { callback.onFailure(response.errorBody()?.string()) }
                    return@launch
                }

                refresh()

                withContext(Dispatchers.Main) { callback.onSuccess() }
            } catch (_: IOException) {
                withContext(Dispatchers.Main) { callback.onFailure(null) }
            }
        }
    }

    fun addTask(title: String, notes: String, beerReward: Int, callback: NetworkResult<String>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.addTask(AddTaskRequest(title, notes, beerReward)).execute()
                if (!response.isSuccessful) {
                    withContext(Dispatchers.Main) { callback.onFailure(response.errorBody()?.string()) }
                    return@launch
                }

                val result = response.body()
                if (result == null) {
                    withContext(Dispatchers.Main) { callback.onFailure(null) }
                    return@launch
                }

                refresh()
                withContext(Dispatchers.Main) { callback.onSuccess(result.data.id) }
            } catch (_: IOException) {
                withContext(Dispatchers.Main) { callback.onFailure(null) }
            }
        }
    }

    fun updateTask(id: String, title: String, notes: String, beerReward: Int, callback: NetworkResultNoData) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // TODO
                refresh()
                withContext(Dispatchers.Main) { callback.onSuccess() }
            } catch (_: IOException) {
                withContext(Dispatchers.Main) { callback.onFailure(null) }
            }
        }
    }

    fun deleteTaskById(id: String, callback: NetworkResultNoData) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.instance.removeTask(id).execute()
            if (!response.isSuccessful) {
                withContext(Dispatchers.Main) { callback.onFailure(response.errorBody()?.string()) }
                return@launch
            }

            refresh()
            withContext(Dispatchers.Main) { callback.onSuccess() }
        }
    }

    fun getTasks(): Flow<List<Task>> {
        return taskDao.getTasks()
    }

    fun getTask(id: String): Flow<Task> {
        return taskDao.getTask(id)
    }


    /**
     *  ShoppingList
     */
    fun getShoppingListItemsFlow(): Flow<List<ShoppingListItem>> {
        return shoppingListItemDao.getShoppingListItemsFlow()
    }

    fun getShoppingListItemById(id: String): Flow<ShoppingListItem> {
        return shoppingListItemDao.getShoppingListItemById(id)
    }


    fun deleteItemFromShoppingList(id: String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.instance.removeShoppingListItem(id).execute()
            if (!response.isSuccessful) {
                Log.e(CoLiPiApplication.LOG_TAG, "Failed to remove shopping list item")
                return@launch
            }

            refresh()
        }
    }

    fun addShoppingListItem(itemTitle: String, itemNotes: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.instance.addShoppingListItem(AddShoppingListItemRequest(itemTitle, itemNotes)).execute()
            if (!response.isSuccessful) {
                Log.e(CoLiPiApplication.LOG_TAG, "Failed to add shopping list item")
                return@launch
            }

            refresh()
        }
    }

    fun checkShoppingListItem(id: String, checkState: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.instance.checkShoppingListItem(id, CheckShoppingListItemRequest(checkState)).execute()
            if (!response.isSuccessful) {
                Log.e(CoLiPiApplication.LOG_TAG, "Failed to change checked state of shopping list item")
                return@launch
            }

            refresh()
        }
    }

    fun updateShoppingListItem(id: String, newTitle: String, newNotes: String) {

        //TODO
    }
}