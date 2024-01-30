package de.fhe.ai.colivingpilot.tasks

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

        //Toolbar title anapassen wenn bearbeiten dann anderer Titel

        toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_newTaskFragment_to_navigation_tasks)
        }


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
                val newTask: ViewTask = ViewTask(
                    title,
                    notes,
                    beerCount
                )
                taskViewModel.configTask(newTask)
                binding.taskNameEditText.setText("")
                findNavController().navigate(R.id.action_newTaskFragment_to_navigation_tasks)

            }

        }
    }
}