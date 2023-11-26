package de.fhe.ai.colivingpilot.wg

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.databinding.FragmentWgBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WgFragment : Fragment() {

    private val viewmodel: WgViewmodel by viewModels()

    private lateinit var binding: FragmentWgBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_wg, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWgBinding.bind(view)

        binding.fabAddUser.setOnClickListener {
            //todo: show addUserDialog
        }

        val userAdapter = UserUiItemAdapter(
            onClick = { username ->
                viewmodel.onEvent(WgEvent.OnClickUser(username))
            },
            onLongClick = { user ->
                viewmodel.onEvent(WgEvent.OnLongClickUser(user))
            }
        )

        viewmodel.userUiItems.observe(viewLifecycleOwner) { userList ->
            userAdapter.userList = userList
            userAdapter.notifyDataSetChanged()
        }

        viewmodel.wgName.observe(viewLifecycleOwner) {
            binding.tvGroupName.text = it
            binding.etGroupName.setText(it)
        }
        viewmodel.wgFragmentState.observe(viewLifecycleOwner) {
            Log.d(
                CoLiPiApplication.LOG_TAG,
                "SettingsFragment FRAGMENT: wgFragmentState.isEditMode = ${it.isEditMode}"
            )
            if (it.isEditMode) {
                //Log.d(CoLiPiApplication.LOG_TAG, "SettingsFragment: wgFragmentState.isEditMode = true")
                binding.tvGroupName.visibility = View.GONE
                binding.etGroupName.visibility = View.VISIBLE
                binding.ibEdit.visibility = View.GONE

                binding.etGroupName.setSelection(binding.etGroupName.text.length)
                binding.etGroupName.requestFocus()
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                imm.showSoftInput(
                    binding.etGroupName,
                    android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
                )
            } else {
                //Log.d(CoLiPiApplication.LOG_TAG, "SettingsFragment: wgFragmentState.isEditMode = false")

                binding.tvGroupName.visibility = View.VISIBLE
                binding.etGroupName.visibility = View.GONE
                binding.ibEdit.visibility = View.VISIBLE
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)

            }
        }

        lifecycleScope.launch {
            viewmodel.uiEvent.collect {
                when (it) {
                    is UiEvent.PopBackStack -> {
                        Log.d(CoLiPiApplication.LOG_TAG, "PopBackStack")
                    }

                    is UiEvent.Navigate -> {
                        Log.d(CoLiPiApplication.LOG_TAG, "Navigate to ${it.route}")
                        when (it.route) {
                            "user" -> {
                                //navigate to user

                            }

                            "settings" -> {
                                //navigate to settings
                                Log.d(CoLiPiApplication.LOG_TAG, "Navigate to settings")
                                findNavController().navigate(R.id.action_navigation_wg_to_navigation_settings)
                            }
                        }
                    }

                    is UiEvent.ShowSnackbar -> {
                        val message = getString(it.message)
                        Log.d(CoLiPiApplication.LOG_TAG, "Show snackbar with message $message")

                        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
                    }

                    is UiEvent.ShowUserLongClickDialog -> {
                        val dialog = UserLongClickDialogFragment(
                            onOkClick = { emoji ->
                                viewmodel.onEvent(
                                    WgEvent.OnDialogOkClick(
                                        it.username,
                                        emoji
                                    )
                                )
                            },
                            onCancelClick = {
                                viewmodel.onEvent(WgEvent.OnDialogCancelClick)
                            },
                            onDeleteClick = { user ->
                                viewmodel.onEvent(WgEvent.OnDeleteUserClick(user))
                            }
                        )
                        val bundle = Bundle()
                        bundle.putString("username", it.username)
                        bundle.putString("id", it.id)
                        dialog.arguments = bundle
                        dialog.show(childFragmentManager, "UserLongClickDialogFragment")
                    }

                    is UiEvent.updateEmoji -> {
                        userAdapter.notifyDataSetChanged()
                    }

                    is UiEvent.updateWgName -> {
                        binding.tvGroupName.text = it.wgName
                    }

                    is UiEvent.activateEditMode -> {
                        binding.tvGroupName.visibility = View.GONE
                        binding.etGroupName.visibility = View.VISIBLE
                        binding.ibEdit.visibility = View.GONE
                        binding.etGroupName.setSelection(binding.etGroupName.text.length)
                        binding.etGroupName.requestFocus()
                        val imm =
                            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                        imm.showSoftInput(
                            binding.etGroupName,
                            android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
                        )
                    }

                    is UiEvent.deactivateEditMode -> {
                        binding.tvGroupName.visibility = View.VISIBLE
                        binding.etGroupName.visibility = View.GONE
                        binding.ibEdit.visibility = View.VISIBLE
                        val imm =
                            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                }
            }
        }

        binding.apply {
            rvSettingsUser.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(requireContext())
            rvSettingsUser.adapter = userAdapter

            etGroupName.setOnEditorActionListener { view, actionId, event ->
                if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                    viewmodel.onEvent(WgEvent.OnChangeWgName(etGroupName.text.toString()))
                    true
                } else {
                    false
                }
            }
            ibEdit.setOnClickListener {
                viewmodel.onEvent(WgEvent.OnClickEditWgButton)
            }

            root.setOnTouchListener(View.OnTouchListener { v, event ->
                if (event.action == android.view.MotionEvent.ACTION_DOWN) {
                    if (binding.etGroupName.visibility == View.VISIBLE) {
                        viewmodel.onEvent(WgEvent.OnClickOutsideEditMode)
                        true
                    }
                }
                false
            })
            ibSettings.setOnClickListener {
                viewmodel.onEvent(WgEvent.OnSettingsClick)
            }
        }
    }

    override fun onDestroy() {
        Log.d(CoLiPiApplication.LOG_TAG, "SettingsFragment: onDestroy()")
        super.onDestroy()

    }

}
