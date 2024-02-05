package de.fhe.ai.colivingpilot.shoppinglist

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.databinding.FragmentShoppingListItemEditDialogBinding
import de.fhe.ai.colivingpilot.network.NetworkResultNoData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class ShoppingListItemEditDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentShoppingListItemEditDialogBinding? = null
    private val shoppingListViewModel: ShoppingListViewModel = ShoppingListViewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShoppingListItemEditDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemId = arguments?.getString("selectedItem") ?: return

        // Starte eine Coroutine im LifecycleScope des Fragments, die ausgeführt wird, wenn der Lifecycle-Status auf STARTED ist
        lifecycleScope.launch {
            // Wiederhole die Coroutine, solange der Lifecycle-Status mindestens STARTED ist
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Rufe das ShoppingListItem von der ViewModel-Methode getShoppingListItemById ab
                shoppingListViewModel.getShoppingListItemById(itemId)
                    // Behandle Fehler, die während des Sammelns des Flows auftreten können
                    .catch { e ->
                        // Logge einen Fehler, wenn das ShoppingListItem nicht abgerufen werden kann
                        Log.e(CoLiPiApplication.LOG_TAG, "Error getting shopping list item by ID", e)
                    }
                    // Sammle die Ergebnisse des Flows, wenn sie verfügbar sind
                    .collect { item ->
                        // Setze den Titel und die Notizen im Binding (UI) basierend auf den Daten des ShoppingListItems
                        binding.editTextTitle.setText(item.title)
                        binding.editTextNotes.setText(item.notes)
                    }
            }
        }

        binding.btnAdd.setOnClickListener {

            val itemTitle = binding.editTextTitle.text.toString()
            val itemNotes = binding.editTextNotes.text.toString()

            if (itemTitle.isNotEmpty()) {
                shoppingListViewModel.updateShoppingListItem(itemId, itemTitle, itemNotes,
                    object : NetworkResultNoData {
                        override fun onSuccess() {
                            findNavController().navigate(R.id.action_shoppingListItemEditDialogFragment_to_navigation_shoppinglist)
                        }

                        override fun onFailure(code: String?) {
                            TODO("Not yet implemented")
                        }
                    })
            } else {
                Snackbar.make(view, "Bitte alle Felder ausfüllen!", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.RED)
                    .show()
            }
        }

        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}