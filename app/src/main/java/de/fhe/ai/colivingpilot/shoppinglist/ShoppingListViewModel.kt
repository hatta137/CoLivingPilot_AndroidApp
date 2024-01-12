package de.fhe.ai.colivingpilot.shoppinglist

import androidx.lifecycle.LiveData
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

            // TODO @hendrik direktes Löschen einbauen
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

