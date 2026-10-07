package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.ItemChooseUsersBinding
import ru.netology.nmedia.databinding.ItemShowUsersBinding
import ru.netology.nmedia.dto.Users
import ru.netology.nmedia.view.loadCircleCrop


class UsersViewHolder(
    private val binding: ItemShowUsersBinding,
    private val onUserClick: (Users) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(user: Users) {
        binding.apply {
            author.text = user.name
            nickname.text = user.login
            avatar.loadCircleCrop(user.avatar)

            root.setOnClickListener {
                onUserClick(user)
            }
        }

    }
}

class UsersAdapter (private val onUserClick: (Users) -> Unit) : RecyclerView.Adapter<UsersViewHolder>() {
    private var users = listOf<Users>()

    fun submitList(user: List<Users>) {
        users = user
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding =
            ItemShowUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolder(binding, onUserClick )
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }
}