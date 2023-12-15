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
import java.util.concurrent.Executors

@Database(entities = [ User::class, Task::class, ShoppingListItem::class, TaskAssignedUser::class ], version = 1)
abstract class WgDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao
    abstract fun shoppingListItemDao(): ShoppingListItemDao
    abstract fun taskAssignedUserDao(): TaskAssignedUserDao

    companion object {

        private var instance: WgDatabase? = null

        private val createCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.i(CoLiPiApplication.LOG_TAG, "Database created")
            }
        }

        fun getDatabase(context: Context): WgDatabase {
            return instance ?: Room.databaseBuilder(context.applicationContext, WgDatabase::class.java, "wg_db")
                .addCallback(createCallback)
                .setQueryCallback({ sqlQuery, bindArgs ->
                    println("SQL Query: $sqlQuery SQL Args: $bindArgs")
                    Log.i("SQL", "Query: $sqlQuery | Args: $bindArgs")
                }, Executors.newSingleThreadExecutor())
                .build()
        }

    }

}