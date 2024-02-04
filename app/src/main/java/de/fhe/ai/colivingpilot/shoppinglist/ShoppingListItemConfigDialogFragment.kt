package de.fhe.ai.colivingpilot.shoppinglist

import android.graphics.Color
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.databinding.FragmentShoppingListItemConfigDialogBinding


class ShoppingListItemConfigDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentShoppingListItemConfigDialogBinding? = null
    private val shoppingListViewModel: ShoppingListViewModel = ShoppingListViewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentShoppingListItemConfigDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            val itemTitle = binding.editTextTitle.text.toString()
            val itemNotes = binding.editTextNotes.text.toString()

            if (itemTitle.isNotEmpty()) {
                shoppingListViewModel.addItemToShoppingList(itemTitle, itemNotes)
                findNavController().navigate(R.id.action_shoppingListItemConfigDialogFragment_to_navigation_shoppinglist)
            } else {
                Snackbar.make(view, "Bitte alle Felder ausfüllen du Huso!", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.RED)
                    .show()
            }
        }

        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_shoppingListItemConfigDialogFragment_to_navigation_shoppinglist)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}