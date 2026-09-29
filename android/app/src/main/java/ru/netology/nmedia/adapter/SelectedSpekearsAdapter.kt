package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.ItemAvatarBinding
import ru.netology.nmedia.dto.Users
import ru.netology.nmedia.view.loadCircleCrop

class SelectedSpeakersViewHolder(
    private val binding: ItemAvatarBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(user: Users) {
        binding.apply {
            avatar.loadCircleCrop(user.avatar)
        }
    }
}

class SelectedSpeakersAdapter : RecyclerView.Adapter<SelectedSpeakersViewHolder>() {
    private var users = listOf<Users>()

    fun submitList(user: List<Users>) {
        users = user.take(5)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectedSpeakersViewHolder {
        val binding =
            ItemAvatarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedSpeakersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectedSpeakersViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }
}