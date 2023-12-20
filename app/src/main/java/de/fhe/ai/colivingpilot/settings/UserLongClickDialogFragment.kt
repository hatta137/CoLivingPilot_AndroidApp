package de.fhe.ai.colivingpilot.settings

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.databinding.DialogUserLongClickBinding

class UserLongClickDialogFragment(
    val onOkClick: (String) -> Unit,
    val onCancelClick: () -> Unit
) : DialogFragment() {

    private lateinit var username: String
    override fun onAttach(context: Context){
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_user_long_click, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        username = arguments?.getString("username").toString()
        return activity?.let{
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val binding = DialogUserLongClickBinding.inflate(inflater)

            val emojiArray = resources.getStringArray(R.array.emoji_array)
            val currentEmoji = CoLiPiApplication.instance.keyValueStore.readString(username + "_emoji")
            val selectedEmoji = emojiArray.indexOf(currentEmoji)
            val spinnerSelectEmojiAdapter = ArrayAdapter.createFromResource(requireParentFragment().requireContext(), R.array.emoji_array, android.R.layout.simple_spinner_item).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            binding.apply {
                spinnerSelectEmoji.adapter = spinnerSelectEmojiAdapter
                spinnerSelectEmoji.setSelection(selectedEmoji)
            }
            builder.setView(binding.root)
                .setPositiveButton(R.string.save) { dialog, id ->
                    val currentSelectedEmoji = binding.spinnerSelectEmoji.selectedItem.toString()
                    onOkClick(currentSelectedEmoji)
                }
                .setNegativeButton(R.string.cancel) { dialog, id ->
                    onCancelClick()
                }
            builder.setTitle("Edit " + username)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }




}


