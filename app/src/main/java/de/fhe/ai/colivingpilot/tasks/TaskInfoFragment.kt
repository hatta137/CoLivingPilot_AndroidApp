package de.fhe.ai.colivingpilot.tasks

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.fhe.ai.colivingpilot.databinding.FragmentTaskInfoBinding
import de.fhe.ai.colivingpilot.tasks.TaskViewModel


/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    navigation_new_task.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class TaskInfoFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentTaskInfoBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel = TaskViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTaskInfoBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //lösen mit id repo funktion aufrufen
        taskViewModel.tasks.observe(viewLifecycleOwner) {
            taskList -> val task = taskList[arguments?.getInt("selectedTask")!!]
            binding.taskTitleTextView.text = task.title
            binding.taskNotesTextView.text = task.notes
            binding.beerCounterTextView.text = task.beerReward.toString().plus(" 🍺")
        }

    }

}