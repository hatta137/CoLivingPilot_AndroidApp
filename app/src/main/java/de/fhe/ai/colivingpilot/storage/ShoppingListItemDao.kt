package de.fhe.ai.colivingpilot.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import de.fhe.ai.colivingpilot.model.ShoppingListItem
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE

@Dao
interface ShoppingListItemDao {

    @Insert
    fun insert(vararg item: ShoppingListItem)

    @Query("SELECT * FROM shopping_list_items")
    fun getShoppingListItemsFlow(): Flow<List<ShoppingListItem>>

    @Delete
    fun deleteItemFromShoppingList(shoppingListItem: ShoppingListItem)


    // TODO UPDATE isChecked
    @Update
    fun updateItem(shoppingListItem: ShoppingListItem)


}