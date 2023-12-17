package de.fhe.ai.colivingpilot.settings

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.databinding.ItemUserBinding
import de.fhe.ai.colivingpilot.model.User


class UserAdapter(
    private val userEvent: UserRecyclerViewEvent
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>(){

    var userList: List<User> = emptyList()

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: User){
            Log.i(CoLiPiApplication.LOG_TAG, "UserAdapter: UserViewHolder.bind()")
            binding.listItemContactName.text = user.username
            binding.listItemContactBeerCount.text = user.beerCounter.toString()
            val sharedPrefs = CoLiPiApplication.instance.getKeyValueStore().preferences
            val emoji = sharedPrefs.getString(user.username.toString() + "_emoji", "")
            binding.listItemContactEmoji.text = emoji
            itemView.setOnClickListener{
                userEvent.onClick(user)
            }
            itemView.setOnLongClickListener{
                userEvent.onLongClick(user)
                true
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.bind(currentUser)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateUserList(newList: List<User>){
        Log.i(CoLiPiApplication.LOG_TAG, "UserAdapter: updateUserList()")
        userList = newList
        notifyDataSetChanged()
    }

    interface UserRecyclerViewEvent{
        fun onClick(user: User)

        fun onLongClick(user: User)
    }
}