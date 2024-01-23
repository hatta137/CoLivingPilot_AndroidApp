package de.fhe.ai.colivingpilot.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.fhe.ai.colivingpilot.R

class Adapter (private var list: ArrayList<String>)
    : RecyclerView.Adapter<Adapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val button: Button

        init {
            textView = view.findViewById(R.id.textView)
            button = view.findViewById(R.id.button)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.textView.text = list[position]
        viewHolder.button.setOnClickListener {
            if(position < list.size) {
                list.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, list.size)
            }
        }

        viewHolder.textView.setOnLongClickListener {
            sort()
        }
    }

    fun addBiggi(biggi: String) {
        //list.add(biggi)
        list.add(biggi)
        notifyItemInserted(list.size)
    }

    private fun sort() : Boolean {
        list.sortBy { it }
        notifyDataSetChanged()
        return true;
    }

    fun reset(newList: ArrayList<String>) {
        list = newList
        notifyDataSetChanged()
    }

    fun clear() : Boolean {
        val size = list.size
        list.clear()
        notifyItemRangeRemoved(0, size)
        return true
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = list.size



}