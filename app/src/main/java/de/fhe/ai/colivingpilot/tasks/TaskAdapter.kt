package de.fhe.ai.colivingpilot.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.model.Task

class TaskAdapter (var items: List<Task>)
    : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private var taskClickListener: TaskClickListener? = null

    fun setOnItemClickListener(listener: TaskClickListener) {
        this.taskClickListener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
        val button: Button = view.findViewById(R.id.button)

        init {
            button.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    taskClickListener?.onItemButtonClick(position)
                }
            }

            view.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    taskClickListener?.onItemLongClick(position)
                }
                return@setOnLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_task, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = items[position].title
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = items.size



}