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
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [ User::class, Task::class, ShoppingListItem::class, TaskAssignedUser::class ],
        version = 4)
abstract class WgDatabase : RoomDatabase() {
        abstract fun userDao(): UserDao
        abstract fun taskDao(): TaskDao
        abstract fun shoppingListItemDao(): ShoppingListItemDao
        abstract fun taskAssignedUserDao(): TaskAssignedUserDao

        class Callback @Inject constructor(
                private val database: Provider<WgDatabase>,
                private val applicationScope: CoroutineScope
        ) : RoomDatabase.Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.i(CoLiPiApplication.LOG_TAG, "Inserting test data...")
                        val dao = database.get().userDao()
                        applicationScope.launch {
                                Log.i(CoLiPiApplication.LOG_TAG, "Inserting test data...")
                                dao.insert(
                                        User(UUID.randomUUID().toString(), "Florian", 69,),
                                        User(UUID.randomUUID().toString(), "Kevin", 420),
                                        User(UUID.randomUUID().toString(), "Hendrik", 1337),
                                        User(UUID.randomUUID().toString(), "Darius", 187))
                        }
                }
        }

}