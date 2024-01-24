package de.fhe.ai.colivingpilot.shoppinglist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import de.fhe.ai.colivingpilot.model.ShoppingListItem
import de.fhe.ai.colivingpilot.storage.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

/***
 * @author Hendrik Lendeckel
 */
class ShoppingListViewModel: ViewModel() {

    private val repository: Repository = Repository()

    // LiveData containing the list of shopping list items
    val shoppingListItems: LiveData<List<ShoppingListItem>> = repository.getShoppingListItemsFlow().asLiveData()


    /**
     * Adds a new shopping list item.
     *
     * @param itemTitle The title of the shopping list item.
     * @param itemNotes Notes for the shopping list item.
     */
    fun addItemToShoppingList(itemTitle: String, itemNotes: String) {

        viewModelScope.launch(Dispatchers.IO) {

            val item = ShoppingListItem(
                UUID.randomUUID().toString(),
                itemTitle,
                itemNotes,
                repository.getTestUser().id, // TODO Test User austauschen durch den gerade angemeldeten
                false)

            repository.insertShoppingListItem(item)
        }
    }

    /**
     * Deletes all completed shopping list items.
     */
    fun deleteDoneItems() {

        viewModelScope.launch(Dispatchers.IO) {

            shoppingListItems.value?.forEach{

                if (it.isChecked){
                    repository.deleteItemFromShoppingList(it)
                }
            }
        }
    }


    /**
     * Updates the status of the shopping list item (selected/unselected).
     *
     * @param shoppingListItem The shopping list item to be updated.
     */
    fun toggleIsChecked(shoppingListItem: ShoppingListItem) {

        viewModelScope.launch(Dispatchers.IO) {

            Log.d("ShoppingListViewModel", "toggleIsChecked ${shoppingListItem.isChecked}")

            if (shoppingListItem != null) {
                repository.updateItem(shoppingListItem, !shoppingListItem.isChecked)
            }
        }
    }
}

