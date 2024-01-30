package de.fhe.ai.colivingpilot.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.fhe.ai.colivingpilot.R


class TasksFragment : Fragment(), TaskClickListener {

    private val taskViewModel : TaskViewModel = TaskViewModel()
    private val taskAdapter = TaskAdapter(mutableListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        taskAdapter.setOnItemClickListener(this)

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

    override fun onItemButtonClick(position: Int) {
        taskViewModel.deleteTask(position)
    }

    override fun onItemLongClick(position: Int) {
        //Toast.makeText(requireContext(), taskAdapter.items[position].notes, Toast.LENGTH_SHORT).show()
        val bundle = Bundle().apply {
            putInt("selectedTask", position)
        }
        findNavController().navigate(R.id.action_navigation_tasks_to_task_info, bundle)
    }

}