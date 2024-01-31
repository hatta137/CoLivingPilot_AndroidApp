package de.fhe.ai.colivingpilot.tasks

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.databinding.FragmentTaskConfigBinding
import de.fhe.ai.colivingpilot.tasks.detail.TaskDetailViewModel


class TaskConfigFragment : Fragment() {

    private var _binding: FragmentTaskConfigBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel = TaskViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskConfigBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar : Toolbar = binding.toolbar

        toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_newTaskFragment_to_navigation_tasks)
        }

        //check ob vorhandener task bearbeitet werden soll
        val selectedTask = arguments?.getString("selectedTask")

        if(selectedTask != null) {
            if(selectedTask.isNotBlank()) {
                toolbar.title = "Aufgabe bearbeiten"
                val taskDetailViewModel = TaskDetailViewModel(selectedTask)
                taskDetailViewModel.task.observe(viewLifecycleOwner) {
                    //als viewtask speichern? mit evtl. funktionen zum be & entstücken der views?
                    binding.taskNameEditText.setText(it.title)
                    binding.notesTextView.setText(it.notes)
                    binding.editBeerCounter.setText(it.beerReward.toString())
                }
            }
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

                val configuredTask = selectedTask?.let {
                    ViewTask(title, notes, beerCount, it)
                } ?: ViewTask(title, notes, beerCount)

                taskViewModel.configTask(configuredTask)
                binding.taskNameEditText.setText("")
                findNavController().navigate(R.id.action_newTaskFragment_to_navigation_tasks)

            }

        }
    }
}