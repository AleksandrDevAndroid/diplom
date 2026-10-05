package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.ItemShowUsersBinding
import ru.netology.nmedia.dto.Users
import ru.netology.nmedia.view.loadCircleCrop


class SelectedParticipantsViewHolder(
    private val binding: ItemShowUsersBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(user: Users) {
        binding.apply {
            author.text = user.name
            nickname.text = user.login
            avatar.loadCircleCrop(user.avatar)
        }
    }
}

class SelectedParticipantsAdapter() : RecyclerView.Adapter<SelectedParticipantsViewHolder>() {
    private var users = listOf<Users>()

    fun submitList(user: List<Users>) {
        users = user
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedParticipantsViewHolder {
        val binding =
            ItemShowUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedParticipantsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectedParticipantsViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }
}