package de.fhe.ai.colivingpilot.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import de.fhe.ai.colivingpilot.model.ShoppingListItem

@Dao
interface ShoppingListItemDao {

    @Insert
    fun insert(vararg item: ShoppingListItem)

    @Query("SELECT * FROM shopping_list_items")
    fun getShoppingListItems(): List<ShoppingListItem>

}