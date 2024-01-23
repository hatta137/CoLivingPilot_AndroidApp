package de.fhe.ai.colivingpilot.tasks

interface TaskClickListener {
    fun onButtonClick(position: Int)

    fun onLongItemClick(position: Int)
}