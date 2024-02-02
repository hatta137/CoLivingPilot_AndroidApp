package de.fhe.ai.colivingpilot.tasks

import android.graphics.Color
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.databinding.FragmentTaskConfigDialogBinding
import de.fhe.ai.colivingpilot.tasks.detail.TaskDetailViewModel


class TaskConfigDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentTaskConfigDialogBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel = TaskViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskConfigDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //check ob vorhandener task bearbeitet werden soll
        val taskId = arguments?.getString("selectedTask")

        if(taskId != null) {
            if(taskId.isNotBlank()) {
                val taskDetailViewModel = TaskDetailViewModel(taskId)
                taskDetailViewModel.task.observe(viewLifecycleOwner) {
                    //als viewtask speichern? mit evtl. funktionen zum be & entstücken der views?
                    binding.taskNameEditText.setText(it.title)
                    binding.notesTextView.setText(it.notes)
                    binding.editBeerCounter.setText(it.beerReward.toString())
                }
            }
        }

        binding.abortButton.setOnClickListener {
            //TODO checken ob das ne gute lösung ist fragment auszublenden?
            this.dismiss()
        }


        binding.addButton.setOnClickListener {

            var title = binding.taskNameEditText.text.toString()
            var notes = binding.notesTextView.text.toString()
            var beerCountText = binding.editBeerCounter.text.toString()

            title = title.trim()
            notes = notes.trim()
            beerCountText = beerCountText.trim()

            if(title.isBlank() || notes.isBlank() || beerCountText.isBlank()) {
                val taskConfigView = view.findViewById<View>(R.id.taskConfigView)
                Snackbar.make(taskConfigView, "Bitte alle Felder ausfüllen!", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.RED)
                    .show()
            }else {

                val beerCount = beerCountText.toInt()

                val configuredTask = taskId?.let {
                    ViewTask(title, notes, beerCount, it)
                } ?: ViewTask(title, notes, beerCount)

                taskViewModel.configTask(configuredTask)

                val bundle = Bundle().apply {
                    putString("selectedTask", configuredTask.id)
                }
                //TODO fixen wen n neu angelegt
                this.dismiss()
                findNavController().navigate(R.id.action_taskConfigDialogFragment_to_task_info, bundle)
            }

        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}