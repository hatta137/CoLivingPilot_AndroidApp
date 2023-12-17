package de.fhe.ai.colivingpilot.settings

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.core.KeyValueStore
import de.fhe.ai.colivingpilot.databinding.FragmentSettingsBinding
import de.fhe.ai.colivingpilot.model.User
import kotlinx.coroutines.launch
import kotlin.concurrent.fixedRateTimer

@AndroidEntryPoint
class SettingsFragment : Fragment(),
    UserAdapter.UserRecyclerViewEvent,
    UserLongClickDialogFragment.UserLongClickDialogListener,
    SharedPreferences.OnSharedPreferenceChangeListener
{
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Log.d(CoLiPiApplication.LOG_TAG, "[Fragment]Shared preferences changed")
        if(key == "wg_name"){
            binding.tvGroupName.text = sharedPreferences?.getString("wg_name", "WG 1")
            binding.etGroupName.setText(sharedPreferences?.getString("wg_name", "WG 1"))
        }
        if (key != null) {
            if(key.contains("_emoji")){
                binding.rvSettingsUser.adapter?.notifyDataSetChanged()
            }
        }
    }
    override fun onClick(user: User) {
        Log.d(CoLiPiApplication.LOG_TAG, "User clicked: ${user.username}")
    }
    override fun onLongClick(user: User) {
        showUserLongClickDialog(user.username)
    }
    fun showUserLongClickDialog(username: String){
        val bundle = Bundle()
        bundle.putString("username", username)
        val dialog = UserLongClickDialogFragment.newInstance(username)
        dialog.show(childFragmentManager, "UserLongClickDialogFragment")
    }
    override fun onDialogCancelClick(dialog: DialogFragment) {
        Log.d(CoLiPiApplication.LOG_TAG, "Dialog canceled")
    }
    override fun onDialogSaveClick(dialog: DialogFragment, username: String, emoji: String) {
        Log.d(CoLiPiApplication.LOG_TAG, "Dialog saved")
        Log.d(CoLiPiApplication.LOG_TAG, "Selected emoji: $emoji")
        viewmodel.saveEmoji(username, emoji)
    }

    private val viewmodel: SettingsViewmodel by viewModels()

    private lateinit var binding: FragmentSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        val userAdapter = UserAdapter(this)
        val sharedPref = CoLiPiApplication.instance.getKeyValueStore()
        sharedPref.registerOnSharedPreferenceChangeListener(this)

        binding.apply {
            rvSettingsUser.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
            rvSettingsUser.adapter = userAdapter
            tvGroupName.text = sharedPref.readString("wg_name")
            etGroupName.setText(sharedPref.readString("wg_name"))

            etGroupName.setOnEditorActionListener{ view, actionId, event ->
                if(actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE){
                    viewmodel.toggleEditMode()
                    viewmodel.updateWgName(etGroupName.text.toString())
                    true
                }else{
                    false
                }
            }

            ibEdit.setOnClickListener{
                viewmodel.toggleEditMode()
            }
        }

        lifecycleScope.launch{
            viewmodel.users.collect{
                userAdapter.updateUserList(it)
                it.forEach{
                    Log.i(CoLiPiApplication.LOG_TAG, "User: ${it.username}")
                }
            }
        }

        viewmodel.editMode.observe(viewLifecycleOwner){editMode ->
            if(editMode){
                binding.etGroupName.apply {
                    visibility = View.VISIBLE
                    setSelection(text.length)
                    if(!hasFocus()){
                        requestFocus()
                    }
                    if(hasFocus()){
                        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                        imm.showSoftInput(this, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT)
                    }
                }
                binding.tvGroupName.visibility = View.GONE
                binding.ibEdit.visibility = View.GONE
            }else{
                binding.etGroupName.visibility = View.GONE
                binding.tvGroupName.visibility = View.VISIBLE
                binding.ibEdit.visibility = View.VISIBLE
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }

            binding.root.setOnTouchListener(View.OnTouchListener { v, event ->
                if (event.action == android.view.MotionEvent.ACTION_DOWN) {
                    if (binding.etGroupName.visibility == View.VISIBLE) {
                        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                        viewmodel.toggleEditMode()
                        true
                    }
                }
                false
            })
        }
    }

    override fun onDestroy() {
        Log.d(CoLiPiApplication.LOG_TAG, "SettingsFragment: onDestroy()")
        super.onDestroy()
        val sharedPref = CoLiPiApplication.instance.getKeyValueStore()
        sharedPref.unregisterOnSharedPreferenceChangeListener(this)
        if(binding.etGroupName.visibility == View.VISIBLE){
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
            viewmodel.toggleEditMode()
        }
    }

}
