package de.fhe.ai.colivingpilot.tasks

interface TaskClickListener {
    fun onItemButtonClick(position: Int)

    fun onItemLongClick(position: Int)
}