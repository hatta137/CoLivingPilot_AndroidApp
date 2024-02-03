package de.fhe.ai.colivingpilot.shoppinglist

import de.fhe.ai.colivingpilot.model.ShoppingListItem

/***
 * @author Hendrik Lendeckel
 */
interface ShoppingListActionListener {
    fun onItemChecked(id: String, isChecked: Boolean)
    fun onItemClicked(item: ShoppingListItem)
}