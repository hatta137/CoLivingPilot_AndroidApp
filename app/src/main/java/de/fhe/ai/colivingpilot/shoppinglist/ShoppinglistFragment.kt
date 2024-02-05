package de.fhe.ai.colivingpilot.shoppinglist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.databinding.FragmentShoppinglistBinding
import de.fhe.ai.colivingpilot.model.ShoppingListItem
import de.fhe.ai.colivingpilot.util.refreshInterface

/***
 * @author Hendrik Lendeckel
 */
class ShoppinglistFragment : Fragment(R.layout.fragment_shoppinglist), ShoppingListActionListener, refreshInterface {

    // Lateinit variables for RecyclerView and Adapter
    private lateinit var binding: FragmentShoppinglistBinding
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

        binding = FragmentShoppinglistBinding.bind(view)

        setupRecyclerView(view)

        binding.swipeRefreshLayout.setOnRefreshListener {
            shoppingListViewModel.refresh()
        }

        binding.btnAddItemToShoppingList.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_shoppinglist_to_shoppingListItemConfigDialogFragment)
        }

        binding.btnDeleteDoneShoppingItems.setOnClickListener {
            shoppingListViewModel.deleteDoneItems()
        }
    }

    // Called when an item is checked in the RecyclerView
    override fun onItemChecked(id: String, isChecked: Boolean) {
        shoppingListViewModel.toggleIsChecked(id, isChecked)
    }

    //Todo fertigstellen
    override fun onItemLongClick(id: String) {
        val bundle = Bundle().apply {
            putString("selectedItem", id)
        }
        findNavController().navigate(R.id.action_navigation_shoppinglist_to_shoppingListItemEditDialogFragment, bundle)
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

        rvShoppingListItems = binding.rvShoppingListItems

        rvShoppingListItems.adapter = shoppingListAdapter
        rvShoppingListItems.layoutManager = LinearLayoutManager(requireContext())

        shoppingListViewModel.shoppingListItems.observe(viewLifecycleOwner) {
            shoppingListAdapter.items = it
            shoppingListAdapter.notifyDataSetChanged()
        }
    }

    override fun refreshFinish() {
        swipeRefreshLayout?.isRefreshing = false
    }
}
