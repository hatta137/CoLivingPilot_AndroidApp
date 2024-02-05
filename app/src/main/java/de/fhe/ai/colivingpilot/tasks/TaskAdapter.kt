package de.fhe.ai.colivingpilot.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.model.Task

class TaskAdapter (private var taskClickListener: TaskClickListener)
    : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    var items: List<Task> = listOf()

    //Bauplan für jeden ViewHolder
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
        var id: String = ""
        val textView: TextView = view.findViewById(R.id.textView)
        private val button: Button = view.findViewById(R.id.button)

        init {
            button.setOnClickListener {
                taskClickListener.onFinishButtonClick(id)
            }

            view.setOnClickListener {
                taskClickListener.onItemClick(id)
            }

            view.setOnLongClickListener {
                taskClickListener.onLongItemClick(id)
                true
            }
        }
    }

    //Dem ViewHolder das Layout zuweisen
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_task, viewGroup, false)
        return ViewHolder(view)
    }

    //Den ViewHolder mit Daten füllen
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = items[position].title
        viewHolder.id = items[position].id
    }

    override fun getItemCount() = items.size
}