package de.fhe.ai.colivingpilot.tasks

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.databinding.FragmentTasksBinding
import de.fhe.ai.colivingpilot.network.NetworkResultNoData
import de.fhe.ai.colivingpilot.util.refreshInterface

class TasksFragment : Fragment(), TaskClickListener, refreshInterface {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel : TaskViewModel = TaskViewModel(this)
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

        binding.swipeRefreshLayout.setOnRefreshListener {
            taskViewModel.refresh()
        }

        binding.addTask.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_tasks_to_taskConfigDialogFragment)
        }

    }

    override fun onFinishButtonClick(id: String) {
        taskViewModel.doneTask(id, object : NetworkResultNoData {
            override fun onSuccess() {
            }

            override fun onFailure(code: String?) {
                view?.let {
                    Snackbar.make(it, "Kein Netz!", Snackbar.LENGTH_LONG)
                        .setBackgroundTint(Color.RED)
                        .show()
                }
            }
        })
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
        findNavController().navigate(R.id.action_navigation_tasks_to_taskConfigDialogFragment, bundle)
    }

    override fun refreshFinish() {
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}