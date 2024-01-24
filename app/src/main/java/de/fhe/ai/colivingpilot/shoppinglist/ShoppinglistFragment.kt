package de.fhe.ai.colivingpilot.shoppinglist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.model.ShoppingListItem
import java.util.UUID

class ShoppinglistFragment : Fragment(), ShoppingListActionListener {

    private lateinit var shoppingListAdapter: ShoppingListAdapter
    private val shoppingListViewModel: ShoppingListViewModel by viewModels()
    private lateinit var rvShoppingListItems: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shoppinglist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)

        val btnAddItem = view.findViewById<Button>(R.id.btnAddItemToShoppingList)
        val btnDeleteDoneTodos = view.findViewById<Button>(R.id.btnDeleteDoneShoppingItems)

        btnAddItem.setOnClickListener {
            showAddItemDialog()
        }

        btnDeleteDoneTodos.setOnClickListener {
            shoppingListViewModel.deleteDoneItems()
        }
    }
    override fun onItemChecked(item: ShoppingListItem) {
        shoppingListViewModel.toggleIsChecked(item)
    }

    override fun onItemClicked(item: ShoppingListItem) {
        val itemPosition = shoppingListAdapter.items.indexOf(item)
        val viewHolder = rvShoppingListItems.findViewHolderForAdapterPosition(itemPosition) as? ShoppingListAdapter.ShoppingListViewHolder
        viewHolder?.let { shoppingListAdapter.toggleNoteVisibility(it) }
    }

    private fun setupRecyclerView(view: View) {
        val itemsList = mutableListOf<ShoppingListItem>()
        shoppingListAdapter = ShoppingListAdapter(requireContext(), this, itemsList)

        rvShoppingListItems = view.findViewById(R.id.rvShoppingListItems)

        rvShoppingListItems.adapter = shoppingListAdapter
        rvShoppingListItems.layoutManager = LinearLayoutManager(requireContext())

        shoppingListViewModel.shoppingListItems.observe(viewLifecycleOwner) {
            shoppingListAdapter.items = it
            shoppingListAdapter.notifyDataSetChanged()
        }
    }

    private fun showAddItemDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_shoppinglist_add_item, null)

        val editTextTitle = dialogView.findViewById<EditText>(R.id.editTextTitle)
        val editTextNotes = dialogView.findViewById<EditText>(R.id.editTextNotes)
        val btnAdd = dialogView.findViewById<Button>(R.id.btnAdd)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle(R.string.dialog_title_add_item)

        val dialog = dialogBuilder.create()

        btnAdd.setOnClickListener {
            val itemTitle = editTextTitle.text.toString()
            val itemNotes = editTextNotes.text.toString()

            if (itemTitle.isNotEmpty()) {
                shoppingListViewModel.addItemToShoppingList(itemTitle, itemNotes)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
