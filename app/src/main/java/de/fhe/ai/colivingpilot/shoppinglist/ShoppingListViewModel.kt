package de.fhe.ai.colivingpilot.shoppinglist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.model.ShoppingListItem
import de.fhe.ai.colivingpilot.network.RetrofitClient
import de.fhe.ai.colivingpilot.network.data.request.AddShoppingListItemRequest
import de.fhe.ai.colivingpilot.storage.Repository
import de.fhe.ai.colivingpilot.util.refreshInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

/***
 * @author Hendrik Lendeckel
 */
class ShoppingListViewModel(val refreshListener: refreshInterface): ViewModel() {

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
        CoLiPiApplication.instance.repository.addShoppingListItem(itemTitle, itemNotes)
    }

    /**
     * Deletes all completed shopping list items.
     */
    fun deleteDoneItems() {
        shoppingListItems.value?.forEach{ item ->
            if (item.isChecked){
                repository.deleteItemFromShoppingList(item)
            }
        }
    }


    /**
     * Updates the status of the shopping list item (selected/unselected).
     *
     * @param shoppingListItem The shopping list item to be updated.
     */
    fun toggleIsChecked(shoppingListItem: ShoppingListItem) {
        CoLiPiApplication.instance.repository.checkShoppingListItem(shoppingListItem, !shoppingListItem.isChecked)
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            CoLiPiApplication.instance.repository.refresh()
            refreshListener.refreshFinish()
        }
    }

}

