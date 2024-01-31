package de.fhe.ai.colivingpilot.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.fhe.ai.colivingpilot.R


class TasksFragment : Fragment(), TaskClickListener {

    private val taskViewModel : TaskViewModel = TaskViewModel()
    private val taskAdapter = TaskAdapter(this)

    //TODO auf binding umbauen

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.adapter = taskAdapter

        val addButton : FloatingActionButton = view.findViewById(R.id.addTask)

        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_tasks_to_newTaskFragment)
        }

        taskViewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.items = it
            taskAdapter.notifyDataSetChanged()
        }
    }

    override fun onFinishButtonClick(id: String) {
        taskViewModel.deleteTask(id)
    }

    override fun onItemClick(id: String) {
        val bundle = Bundle().apply {
            putString("selectedTask", id)
        }
        findNavController().navigate(R.id.action_navigation_tasks_to_task_info, bundle)
    }

    override fun onLongItemClick(id: String) {
        val bundle = Bundle().apply {
            putString("selectedTask", id)
        }
        findNavController().navigate(R.id.action_navigation_tasks_to_newTaskFragment, bundle)
    }

}