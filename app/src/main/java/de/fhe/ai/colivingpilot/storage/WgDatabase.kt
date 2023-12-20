package de.fhe.ai.colivingpilot.storage

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.model.ShoppingListItem
import de.fhe.ai.colivingpilot.model.Task
import de.fhe.ai.colivingpilot.model.TaskAssignedUser
import de.fhe.ai.colivingpilot.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.UUID
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [ User::class, Task::class, ShoppingListItem::class, TaskAssignedUser::class ],
        version = 4)
abstract class WgDatabase : RoomDatabase() {

        private var instance: WgDatabase? = null
        abstract fun userDao(): UserDao
        abstract fun taskDao(): TaskDao
        abstract fun shoppingListItemDao(): ShoppingListItemDao
        abstract fun taskAssignedUserDao(): TaskAssignedUserDao

        companion object {
                @Volatile
                private var instance: WgDatabase? = null

                fun getInstance(context: Context): WgDatabase {
                        return instance ?: synchronized(this) {
                                instance ?: buildDatabase(context).also { instance = it }
                        }
                }

                private fun buildDatabase(context: Context): WgDatabase {
                        return Room.databaseBuilder(context.applicationContext, WgDatabase::class.java, "wg_db")
                                .addCallback(createCallback)
                                .build()
                }

                private val createCallback = object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                Log.i(CoLiPiApplication.LOG_TAG, "Database created")
                                val userDao = getInstance(CoLiPiApplication.applicationContext()).userDao()
                                CoroutineScope(Dispatchers.IO).launch {
                                        userDao.insert(
                                                User(
                                                        UUID.randomUUID().toString(),
                                                        "Kevin",
                                                        420
                                                ),
                                                User(
                                                        UUID.randomUUID().toString(),
                                                        "Darius",
                                                        187
                                                ),
                                                User(
                                                        UUID.randomUUID().toString(),
                                                        "Hendrik",
                                                        1337
                                                ),
                                                User(
                                                        UUID.randomUUID().toString(),
                                                        "Florian",
                                                        69
                                                ))
                                }

                        }
                }
        }



}