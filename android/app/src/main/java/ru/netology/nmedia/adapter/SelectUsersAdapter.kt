package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.ItemChooseUsersBinding
import ru.netology.nmedia.dto.Users
import ru.netology.nmedia.view.loadCircleCrop


class SelectUserViewHolder(
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

class SelectUsersAdapter(
    private val onChecked: (Users, Boolean) -> Unit
) : RecyclerView.Adapter<SelectUserViewHolder>() {
    private var users = listOf<Users>()


    fun submitList(user: List<Users>) {
        users = user
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectUserViewHolder {
        val binding =
            ItemChooseUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectUserViewHolder(onChecked, binding)
    }

    override fun onBindViewHolder(holder: SelectUserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }
}