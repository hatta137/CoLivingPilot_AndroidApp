package de.fhe.ai.colivingpilot.wg

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.databinding.DialogUserLongClickBinding


class UserLongClickDialogFragment(
) : DialogFragment() {

    private val viewmodel: UserLongClickViewmodel by viewModels({requireParentFragment()})
    private lateinit var username: String
    private lateinit var id: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        username = viewmodel.username
        id = viewmodel.id
        Log.i("UserLongClickDialog", "username: " + username + " id: " + id)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val binding = DialogUserLongClickBinding.inflate(inflater)

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
                spinnerSelectEmoji.adapter = spinnerSelectEmojiAdapter
                spinnerSelectEmoji.setSelection(selectedEmoji)
                buttonDeleteUser.setOnClickListener {
                    viewmodel.onDeleteUserClick()
                }
            }
            builder.setView(binding.root)
                .setPositiveButton(R.string.save) { dialog, id ->
                    val currentSelectedEmoji = binding.spinnerSelectEmoji.selectedItem.toString()
                    viewmodel.onDialogOkClick(currentSelectedEmoji)
                }
                .setNegativeButton(R.string.cancel) { dialog, id ->
                    viewmodel.onDialogCancelClick()
                }
            builder.setTitle("Edit " + username)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}


