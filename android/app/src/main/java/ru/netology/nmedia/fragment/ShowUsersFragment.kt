package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.netology.nmedia.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nmedia.adapter.UsersAdapter
import ru.netology.nmedia.databinding.FragmentShowUsersBinding
import ru.netology.nmedia.viewmodel.RegisterViewModel

class ShowUsersFragment : Fragment() {
    private val registerViewModel: RegisterViewModel by activityViewModels()
    private val adapter = UsersAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentShowUsersBinding.inflate(layoutInflater, container, false)

        registerViewModel.getUsers()

        binding.usersRecycleView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ShowUsersFragment.adapter
        }

        registerViewModel.users.observe(viewLifecycleOwner){ users ->
            adapter.submitList(users)
        }

        binding.tabEvent.setOnClickListener {
            findNavController().navigate(R.id.action_show_users_to_event_feed)
        }

        binding.tabPosts.setOnClickListener {
            findNavController().navigate(ru.netology.nmedia.R.id.action_show_users_to_feedFragment)
        }
        return binding.root
    }
}