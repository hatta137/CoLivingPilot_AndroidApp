package de.fhe.ai.colivingpilot.shoppinglist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.model.ShoppingListItem
import de.fhe.ai.colivingpilot.util.refreshInterface

/***
 * @author Hendrik Lendeckel
 */
class ShoppinglistFragment : Fragment(), ShoppingListActionListener, refreshInterface {

    // Lateinit variables for RecyclerView and Adapter
    private lateinit var shoppingListAdapter: ShoppingListAdapter
    private lateinit var rvShoppingListItems: RecyclerView
    private val shoppingListViewModel: ShoppingListViewModel = ShoppingListViewModel(this)
    private var swipeRefreshLayout : SwipeRefreshLayout? = null;

    // Called when the view is created
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflating the layout for this fragment
        return inflater.inflate(R.layout.fragment_shoppinglist, container, false)
    }

    // Called after the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)

        val btnAddItem = view.findViewById<FloatingActionButton>(R.id.btnAddItemToShoppingList)
        val btnDeleteDoneTodos = view.findViewById<FloatingActionButton>(R.id.btnDeleteDoneShoppingItems)

        swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

        swipeRefreshLayout?.setOnRefreshListener {
            shoppingListViewModel.refresh()
        }

        btnAddItem.setOnClickListener {
            showAddItemDialog()
        }

        btnDeleteDoneTodos.setOnClickListener {
            shoppingListViewModel.deleteDoneItems()
        }
    }

    // Called when an item is checked in the RecyclerView
    override fun onItemChecked(item: ShoppingListItem) {
        shoppingListViewModel.toggleIsChecked(item)
    }

    // Called when an item is clicked in the RecyclerView
    override fun onItemClicked(item: ShoppingListItem) {
        val itemPosition = shoppingListAdapter.items.indexOf(item)
        val viewHolder = rvShoppingListItems.findViewHolderForAdapterPosition(itemPosition) as? ShoppingListAdapter.ShoppingListViewHolder
        viewHolder?.let { shoppingListAdapter.toggleNoteVisibility(it) }
    }

    // Setting up the RecyclerView and related components
    @SuppressLint("NotifyDataSetChanged")
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

    // Showing the dialog to add a new shopping item
    private fun showAddItemDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_shoppinglist_add_item, null)

        val editTextTitle = dialogView.findViewById<EditText>(R.id.editTextTitle)
        val editTextNotes = dialogView.findViewById<EditText>(R.id.editTextNotes)
        val btnAdd = dialogView.findViewById<FloatingActionButton>(R.id.btnAdd)
        val btnCancel = dialogView.findViewById<FloatingActionButton>(R.id.btnCancel)

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        val dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


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

    override fun refreshFinish() {
        swipeRefreshLayout?.isRefreshing = false
    }
}
