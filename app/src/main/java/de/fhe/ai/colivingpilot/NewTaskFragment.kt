package de.fhe.ai.colivingpilot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

            val newTask : TaskViewModel.NewTask = TaskViewModel.NewTask(
                binding.taskNameEditText.text.toString(),
                binding.notesTextView.text.toString(),
                binding.editBeerCounter.text.toString().toInt())

            taskViewModel.addTask(newTask)
            binding.taskNameEditText.setText("")
            findNavController().navigate(R.id.action_newTaskFragment_to_navigation_tasks)


        }
    }
}