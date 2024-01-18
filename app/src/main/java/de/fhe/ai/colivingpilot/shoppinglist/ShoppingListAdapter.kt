package de.fhe.ai.colivingpilot.shoppinglist

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.model.ShoppingListItem

class ShoppingListAdapter(
    private val context: Context,
    var items: List<ShoppingListItem>
) : RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {


    class ShoppingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItemTitle: TextView = itemView.findViewById(R.id.tvItemTitle)
        val cbDone: CheckBox = itemView.findViewById(R.id.cbDone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {

        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.shoppinglist_item, parent, false)

        return ShoppingListViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val curItem = items[position]

        holder.tvItemTitle.text = curItem.title
        holder.cbDone.isChecked = curItem.isChecked

        toggleStrikeThrough(holder.tvItemTitle, curItem.isChecked)

        holder.cbDone.setOnCheckedChangeListener { _, isChecked ->
            toggleStrikeThrough(holder.tvItemTitle, isChecked)
            curItem.isChecked = !curItem.isChecked
            // TODO @hendrik isChecked in DB schreiben
            // ViewModel funktion benutzen
        }
    }

    private fun toggleStrikeThrough(tvItemTitle: TextView, isChecked: Boolean) {
        if(isChecked) {
            tvItemTitle.paintFlags = tvItemTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            tvItemTitle.paintFlags = tvItemTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}