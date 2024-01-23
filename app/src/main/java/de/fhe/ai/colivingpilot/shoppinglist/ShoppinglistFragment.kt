package de.fhe.ai.colivingpilot.shoppinglist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.model.ShoppingListItem
import java.util.UUID

class ShoppinglistFragment : Fragment() {

    private lateinit var shoppingListAdapter: ShoppingListAdapter
    private val shoppingListViewModel: ShoppingListViewModel by viewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shoppinglist, container, false)

        shoppingListAdapter = ShoppingListAdapter(requireContext(), shoppingListViewModel, mutableListOf())

        // Recycler View
        val rvShoppingListItems = view.findViewById<RecyclerView>(R.id.rvShoppingListItems)

        rvShoppingListItems.adapter = shoppingListAdapter

        rvShoppingListItems.layoutManager = LinearLayoutManager(requireContext())

        val btnAddItem = view.findViewById<Button>(R.id.btnAddItemToShoppingList)
        val etItemTitle = view.findViewById<EditText>(R.id.etItemTitle)
        val btnDeleteDoneTodos = view.findViewById<Button>(R.id.btnDeleteDoneShoppingItems)
        val cbDone = view.findViewById<CheckBox>(R.id.cbDone)

        btnAddItem.setOnClickListener {
            val itemTitle = etItemTitle.text.toString()
            if (itemTitle.isNotEmpty()) {
                shoppingListViewModel.addItemToShoppingList(itemTitle)
                etItemTitle.text.clear()
            }
        }

        btnDeleteDoneTodos.setOnClickListener {
            shoppingListViewModel.deleteDoneItems()
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        shoppingListViewModel.shoppingListItems.observe(viewLifecycleOwner) {

            shoppingListAdapter.items = it
            shoppingListAdapter.notifyDataSetChanged()
        }
    }
}
