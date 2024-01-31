package de.fhe.ai.colivingpilot.tasks.detail

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.fhe.ai.colivingpilot.databinding.FragmentTaskDetailBinding

class TaskDetailFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val taskDetailViewModel = TaskDetailViewModel(arguments?.getString("selectedTask")!!)

        taskDetailViewModel.task.observe(viewLifecycleOwner) { task ->
            binding.taskTitleTextView.text = task.title
            binding.taskNotesTextView.text = task.notes
            binding.beerCounterTextView.text = task.beerReward.toString().plus(" 🍺")
        }


    }

}