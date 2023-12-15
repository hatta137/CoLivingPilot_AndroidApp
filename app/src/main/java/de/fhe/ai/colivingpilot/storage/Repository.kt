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
        userDao.deleteAll() // triggers cascading deletes in all tables
        val response = RetrofitClient.instance.getWgData().execute()

        if (response.isSuccessful) {
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
        } else {
            Log.e(CoLiPiApplication.LOG_TAG, "Failed to fetch WG data")
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

    /*
    val LOG_TAG = "ContactRepository"

    private var contactDao: ContactDao? = null
    private var beerDao: BeerDao? = null

    fun Repository(context: Context?) {
        val db: ContactDatabase = ContactDatabase.getDatabase(context)
        contactDao = db.contactDao()
        beerDao = db.beerDao()
    }

    fun getBeers(): List<Beer?>? {
        return query(Callable<List<T>> { beerDao.getBeers() })
    }

    fun getContacts(): List<Contact?>? {
        return query(Callable<List<T>> { contactDao.getContacts() })
    }

    fun getContactsForLastname(search: String?): List<Contact?>? {
        return query(Callable<List<T>> {
            contactDao.getContactsForLastname(
                search
            )
        })
    }

    fun getContactsSortedByLastname(): List<Contact?>? {
        return query(Callable<List<T>> { contactDao.getContactSortedByLastname() })
    }

    private fun <T> query(query: Callable<List<T>>): List<T>? {
        try {
            return ContactDatabase.query(query)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return ArrayList()
    }

    fun getLastContact(): Contact? {
        try {
            return ContactDatabase.query(contactDao::getLastEntry)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return Contact("", "")
    }

    fun update(contact: Contact) {
        contact.setModified(System.currentTimeMillis())
        contact.setVersion(contact.getVersion() + 1)
        ContactDatabase.execute { contactDao.update(contact) }
    }

    fun insert(contact: Contact) {
        contact.setCreated(System.currentTimeMillis())
        contact.setModified(contact.getCreated())
        contact.setVersion(1)
        ContactDatabase.execute { contactDao.insert(contact) }
    }
    */

}