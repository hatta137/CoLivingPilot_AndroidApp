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

    // ViewHolder creation
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shoppinglist_item, parent, false)
        return ShoppingListViewHolder(view)
    }

    // Binding data to ViewHolder
    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val curItem = items[position]

        // Log statement for debugging
        // Log.d("ShoppingListAdapter", "onBindViewHolder called for position $position")

        // Set item data to views
        holder.tvItemTitle.text = curItem.title
        holder.cbDone.isChecked = curItem.isChecked
        holder.tvNotePreview.text = curItem.notes
        holder.tvFullNote.text = curItem.notes

        // Set listener for CheckBox changes
        holder.cbDone.setOnCheckedChangeListener { _, isChecked ->
            toggleStrikeThrough(holder.tvItemTitle, isChecked)
            listener.onItemChecked(curItem)
        }

        // Set listener for item click
        holder.itemView.setOnClickListener {
            listener.onItemClicked(curItem)
        }
    }

    // Toggle strike through for the title TextView
    private fun toggleStrikeThrough(tvItemTitle: TextView, isChecked: Boolean) {

        if(isChecked) {
            tvItemTitle.paintFlags = tvItemTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            tvItemTitle.paintFlags = tvItemTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    // Toggle tvNotePreview and tvFullNote in the ViewHolder
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

    override fun getItemCount(): Int {
        return items.size
    }
}