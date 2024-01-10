package de.fhe.ai.colivingpilot.shoppinglist

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import de.fhe.ai.colivingpilot.core.KeyValueStore
import de.fhe.ai.colivingpilot.model.ShoppingListItem
import de.fhe.ai.colivingpilot.storage.Repository
import de.fhe.ai.colivingpilot.storage.StaticUUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class ShoppingListViewModel: ViewModel() {

    private val repository: Repository = Repository()

    // LiveData für die Shopping-Liste
    val shoppingListItems: LiveData<List<ShoppingListItem>> = repository.getShoppingListItemsFlow().asLiveData()

    // Funktion zum Hinzufügen eines Elements zur Einkaufsliste
    fun addItemToShoppingList(itemTitle: String) {

        val staticId = StaticUUID()
        val testID = staticId.getID()

        val item = ShoppingListItem(
            UUID.randomUUID().toString(),
            itemTitle,
            "notes",
            testID,
            false)

        viewModelScope.launch(Dispatchers.IO) {
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

    //TODO upadte aufrufen is checked -> Funktion
    fun toggleIsChecked(item: ShoppingListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            if (item.isChecked) {
                repository.updateItem(item, false)
            } else {
                repository.updateItem(item, true)
            }
        }
    }
}

