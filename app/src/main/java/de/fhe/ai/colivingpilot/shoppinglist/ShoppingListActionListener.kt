package de.fhe.ai.colivingpilot.shoppinglist

import de.fhe.ai.colivingpilot.model.ShoppingListItem

interface ShoppingListActionListener {
    fun onItemChecked(item: ShoppingListItem)
    fun onItemClicked(item: ShoppingListItem)
}