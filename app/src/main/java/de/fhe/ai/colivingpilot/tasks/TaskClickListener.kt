package de.fhe.ai.colivingpilot.tasks

interface TaskClickListener {
    fun onFinishButtonClick(id: String)
    fun onItemClick(id: String)
    fun onLongItemClick(id: String)
}