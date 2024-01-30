package de.fhe.ai.colivingpilot

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import de.fhe.ai.colivingpilot.tasks.TaskViewModel
import de.fhe.ai.colivingpilot.databinding.FragmentNewTaskBinding


class NewTaskFragment : Fragment() {

    private var _binding: FragmentNewTaskBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel : TaskViewModel = TaskViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener {

            var title = binding.taskNameEditText.text.toString()
            var notes = binding.notesTextView.text.toString()
            var beerCountText = binding.editBeerCounter.text.toString()

            title = title.trim()
            notes = notes.trim()
            beerCountText = beerCountText.trim()

            if(title.isBlank() || notes.isBlank() || beerCountText.isBlank()) {
                val snackbar = Snackbar.make(view, "Bitte alle Felder ausfüllen!", 3000)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            }else {
                val beerCount = beerCountText.toInt()
                val newTask: TaskViewModel.NewTask = TaskViewModel.NewTask(
                    title,
                    notes,
                    beerCount
                )
                taskViewModel.addTask(newTask)
                binding.taskNameEditText.setText("")
                findNavController().navigate(R.id.action_newTaskFragment_to_navigation_tasks)
            }

        }
    }
}