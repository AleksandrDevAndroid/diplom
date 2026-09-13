package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.ItemChooseUsersBinding
import ru.netology.nmedia.dto.Users
import ru.netology.nmedia.view.loadCircleCrop


class ChooseUserViewHolder(
    private val onChecked: (Users, Boolean) -> Unit,
    private val binding: ItemChooseUsersBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: Users) {
        binding.apply {
            author.text = user.name
            nickname.text = user.login
            avatar.loadCircleCrop(user.avatar)
            checkbox.setOnCheckedChangeListener(null)
            checkbox.isChecked = user.isSelected
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                user.isSelected = isChecked
                onChecked(user, isChecked)
            }
            root.setOnClickListener {
                checkbox.isChecked = !checkbox.isChecked
            }

        }
    }
}

class ChooseUsersAdapter(
    private val onChecked: (Users, Boolean) -> Unit
) : RecyclerView.Adapter<ChooseUserViewHolder>() {
    private var users = listOf<Users>()


    fun submitList(user: List<Users>) {
        users = user
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseUserViewHolder {
        val binding =
            ItemChooseUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChooseUserViewHolder(onChecked, binding)
    }

    override fun onBindViewHolder(holder: ChooseUserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }
}