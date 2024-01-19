package de.fhe.ai.colivingpilot.wg

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.databinding.ItemUserBinding
import android.view.ViewGroup
import android.view.LayoutInflater
class UserUiItemAdapter(
    private val onClick : (String) -> Unit,
    private val onLongClick : (UserUiItem) -> Unit

) : RecyclerView.Adapter<UserUiItemAdapter.UserUiItemViewHolder>(){

    var userList: List<UserUiItem> = emptyList()

    inner class UserUiItemViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: UserUiItem){
            Log.i(CoLiPiApplication.LOG_TAG, "UserAdapter: UserViewHolder.bind()")
            binding.listItemContactName.text = user.username
            binding.listItemContactBeerCount.text = user.beerCount.toString()
            val sharedPrefs = CoLiPiApplication.instance.getKeyValueStore().preferences
            val emoji = sharedPrefs.getString(user.username.toString() + "_emoji", "")
            binding.listItemContactEmoji.text = emoji
            itemView.setOnClickListener{
                onClick(user.username)
            }
            itemView.setOnLongClickListener{
                onLongClick(user)
                true
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserUiItemViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserUiItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserUiItemAdapter.UserUiItemViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.bind(currentUser)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

}