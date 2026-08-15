package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.databinding.ItemUsersBinding
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.Users
import ru.netology.nmedia.view.loadCircleCrop


class UsersAdapter(
    private val onUserClick: (Users) -> Unit
) : ListAdapter<Users, UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding, onUserClick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class UserViewHolder (
    private val binding: ItemUsersBinding,
    private val onUserClick: (Users) -> Unit
) : RecyclerView.ViewHolder(binding.root){
    fun bind(user : Users) {
        binding.apply {
            author.text = user.name
            nickname.text = user.nickname
            avatar.loadCircleCrop("${BuildConfig.BASE_URL}/users/${user.avatar}")

            checkbox.isChecked = user.isSelected
            root.setOnClickListener {
                user.isSelected = !user.isSelected
                checkbox.isChecked = user.isSelected
                onUserClick(user)
            }
        }
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<Users>() {
    override fun areItemsTheSame(oldItem: Users, newItem: Users): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Users, newItem: Users): Boolean {
        return oldItem == newItem
    }
}
