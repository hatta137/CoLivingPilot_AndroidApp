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


class NewTaskFragment : Fragment() {

    private val taskViewModel : TaskViewModel = TaskViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addButton : Button = view.findViewById(R.id.addButton)
        val newTaskEditText : TextView = view.findViewById(R.id.taskNameEditText)

        addButton.setOnClickListener {

            val newTask : TaskViewModel.NewTask = TaskViewModel.NewTask(newTaskEditText.text.toString(), "", 1)

            taskViewModel.addTask(newTask)
            newTaskEditText.text = ""
            findNavController().navigate(R.id.action_newTaskFragment_to_navigation_tasks)


        }
    }
}