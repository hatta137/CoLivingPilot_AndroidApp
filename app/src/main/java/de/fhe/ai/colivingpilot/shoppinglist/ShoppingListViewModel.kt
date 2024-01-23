package de.fhe.ai.colivingpilot.shoppinglist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import de.fhe.ai.colivingpilot.model.ShoppingListItem
import de.fhe.ai.colivingpilot.storage.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class ShoppingListViewModel: ViewModel() {

    private val repository: Repository = Repository()

    // LiveData für die Shopping-Liste
    val shoppingListItems: LiveData<List<ShoppingListItem>> = repository.getShoppingListItemsFlow().asLiveData()


    // Funktion zum Hinzufügen eines Elements zur Einkaufsliste
    fun addItemToShoppingList(itemTitle: String) {

        viewModelScope.launch(Dispatchers.IO) {

            // TODO @hendrik Notes einfügen
            val item = ShoppingListItem(
                UUID.randomUUID().toString(),
                itemTitle,
                "notes",
                repository.getTestUser().id,
                false)

            repository.insertShoppingListItem(item)
        }
    }

    // Funktion zum Löschen erledigter Elemente aus der Einkaufsliste
    fun deleteDoneItems() {

        viewModelScope.launch(Dispatchers.IO) {

            shoppingListItems.value?.forEach{

                if (it.isChecked){
                    repository.deleteItemFromShoppingList(it)
                }
            }
        }
    }

    //TODO Checkbox check failt
    fun toggleIsChecked(position: Int) {

        viewModelScope.launch(Dispatchers.IO) {

            val itemList: List<ShoppingListItem>? = shoppingListItems.value

            val desiredItem: ShoppingListItem? = itemList?.get(position)
            //Log.d("ShoppingListViewModel", "desiredItem $desiredItem?")
            Log.d("ShoppingListViewModel", "toggleIsChecked ${desiredItem?.isChecked}")

            if (desiredItem != null) {
                if (desiredItem.isChecked){
                    repository.updateItem(desiredItem, false)
                } else {
                    repository.updateItem(desiredItem, true)
                }
            }
        }
    }
}

