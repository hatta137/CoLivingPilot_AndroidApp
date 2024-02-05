package de.fhe.ai.colivingpilot.shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import de.fhe.ai.colivingpilot.model.ShoppingListItem
import de.fhe.ai.colivingpilot.network.NetworkResultNoData
import de.fhe.ai.colivingpilot.storage.Repository
import de.fhe.ai.colivingpilot.util.refreshInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/***
 * @author Hendrik Lendeckel
 */
class ShoppingListViewModel(val refreshListener: refreshInterface? = null): ViewModel() {

    private val repository: Repository = Repository()

    // LiveData containing the list of shopping list items
    val shoppingListItems: LiveData<List<ShoppingListItem>> = repository.getShoppingListItemsFlow().asLiveData()


    /**
     * Adds a new shopping list item.
     *
     * @param title The title of the shopping list item.
     * @param notes Notes for the shopping list item.
     */
    fun addItemToShoppingList(title: String, notes: String, callback: NetworkResultNoData) {
        repository.addShoppingListItem(title, notes, callback)
    }

    fun updateShoppingListItem(id: String, title: String, notes: String, callback: NetworkResultNoData) {
        repository.updateShoppingListItem(id, title, notes, callback)
    }

    /**
     * Deletes all completed shopping list items.
     */
    fun deleteDoneItems() {
        shoppingListItems.value?.forEach{ item ->
            if (item.isChecked){
                repository.deleteItemFromShoppingList(item.id, object : NetworkResultNoData {
                    override fun onSuccess() {
                    }

                    override fun onFailure(code: String?) {

                    }
                })
            }
        }
    }

    /**
     * Updates the status of the shopping list item (selected/unselected).
     *
     * @param shoppingListItem The shopping list item to be updated.
     */
    fun toggleIsChecked(id: String, isChecked: Boolean, callback: NetworkResultNoData) {
        repository.checkShoppingListItem(id, !isChecked, callback)
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.refresh()
            refreshListener?.refreshFinish()
        }
    }

    fun getShoppingListItemById(id: String): Flow<ShoppingListItem> {
        return repository.getShoppingListItemById(id)
    }

}

