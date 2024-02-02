package de.fhe.ai.colivingpilot.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.databinding.FragmentTasksBinding


class TasksFragment : Fragment(), TaskClickListener {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel : TaskViewModel = TaskViewModel()
    private val taskAdapter = TaskAdapter(this)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = taskAdapter
        taskViewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.items = it
            taskAdapter.notifyDataSetChanged()
        }

        binding.addTask.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_tasks_to_taskConfigDialogFragment)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}