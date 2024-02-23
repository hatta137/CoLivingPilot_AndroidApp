package de.fhe.ai.colivingpilot.shoppinglist

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.model.ShoppingListItem

/***
 * @author Hendrik Lendeckel
 */
class ShoppingListAdapter(
    private val context: Context,
    private val listener: ShoppingListActionListener,
    var items: List<ShoppingListItem>
) : RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {

    class ShoppingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItemTitle: TextView = itemView.findViewById(R.id.tvItemTitle)
        val cbDone: CheckBox = itemView.findViewById(R.id.cbDone)
        val tvNotePreview: TextView = itemView.findViewById(R.id.tvNotePreview)
        val tvFullNote: TextView = itemView.findViewById(R.id.tvFullNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shoppinglist_item, parent, false)
        return ShoppingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {

        val curItem = items[position]

        holder.tvItemTitle.text = curItem.title
        holder.cbDone.isChecked = curItem.isChecked
        holder.tvNotePreview.text = curItem.notes
        holder.tvFullNote.text = curItem.notes

        holder.cbDone.setOnCheckedChangeListener { _, isChecked ->
            listener.onItemChecked(curItem.id, curItem.isChecked)
        }

        holder.itemView.setOnClickListener {
            listener.onItemClicked(curItem)
        }

        holder.itemView.setOnLongClickListener{
            listener.onItemLongClick(curItem.id)
            true
        }

        toggleStrikeThrough(holder.tvItemTitle, holder.cbDone.isChecked)
    }

    private fun toggleStrikeThrough(tvItemTitle: TextView, isChecked: Boolean) {

        if(isChecked) {
            tvItemTitle.paintFlags = tvItemTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            tvItemTitle.paintFlags = tvItemTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    fun toggleNoteVisibility(holder: ShoppingListViewHolder) {

        val notePreviewVisible = holder.tvNotePreview.visibility == View.VISIBLE
        val fullNoteVisible = holder.tvFullNote.visibility == View.VISIBLE

        if (notePreviewVisible) {
            holder.tvNotePreview.visibility = View.GONE
            holder.tvFullNote.visibility = View.VISIBLE
        } else if (fullNoteVisible) {
            holder.tvNotePreview.visibility = View.VISIBLE
            holder.tvFullNote.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int { return this.items.size }

    override fun getItemId(position: Int): Long { return position.toLong() }

    override fun getItemViewType(position: Int): Int { return position }
}