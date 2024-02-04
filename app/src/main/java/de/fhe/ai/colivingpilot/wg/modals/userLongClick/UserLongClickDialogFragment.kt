package de.fhe.ai.colivingpilot.wg.modals.userLongClick

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.databinding.DialogUserLongClickBinding


class UserLongClickDialogFragment(
) : BottomSheetDialogFragment() {

    private val viewmodel: UserLongClickViewmodel by viewModels()
    private lateinit var username: String
    private lateinit var id: String
    private var _binding: DialogUserLongClickBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = DialogUserLongClickBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            username = it.getString("username").toString()
            id = it.getString("id").toString()
        }
        Log.i("UserLongClickDialog", "username: " + username + " id: " + id)

        val emojiArray = resources.getStringArray(R.array.emoji_array)
        val currentEmoji =
            CoLiPiApplication.instance.keyValueStore.readString(username + "_emoji")
        val selectedEmoji = emojiArray.indexOf(currentEmoji)
        val spinnerSelectEmojiAdapter = ArrayAdapter.createFromResource(
            requireParentFragment().requireContext(),
            R.array.emoji_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.apply {
            spinnerSelectEmoji.apply {
                adapter = spinnerSelectEmojiAdapter
                setSelection(selectedEmoji)
            }
            buttonDeleteUser.apply {
                setOnClickListener {
                    viewmodel.onDeleteUserClick(id.toString())
                }
                text = username + " aus WG entfernen"
            }
            abortButton.apply {
                setOnClickListener { dismiss() }
            }
            acceptButton.apply {
                setOnClickListener {
                    Log.i(
                        "UserLongClickDialog",
                        "selectedEmoji: " + spinnerSelectEmoji.selectedItem.toString()
                    )
                    viewmodel.onDialogOkClick(
                        spinnerSelectEmoji.selectedItem.toString(),
                        username
                    )
                    dismiss()
                }

            }
        }

    }
}




