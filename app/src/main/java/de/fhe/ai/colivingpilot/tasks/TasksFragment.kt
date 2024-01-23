package de.fhe.ai.colivingpilot.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
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



        val nameTextView: TextView = view.findViewById(R.id.nameEditText)
        val joinButton : Button = view.findViewById(R.id.join)

        joinButton.setOnClickListener {
            taskViewModel.addTask(nameTextView.text.toString())
        }

        taskViewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.items = it
            taskAdapter.notifyDataSetChanged()
        }
    }

    override fun onButtonClick(position: Int) {
        taskViewModel.deleteTask(position)
    }

    override fun onLongItemClick(position: Int) {
        Toast.makeText(requireContext(), taskAdapter.items[position].notes, Toast.LENGTH_SHORT).show()
    }

}