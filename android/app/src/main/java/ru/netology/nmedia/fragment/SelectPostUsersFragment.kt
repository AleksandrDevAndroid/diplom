package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.adapter.SelectUsersAdapter
import ru.netology.nmedia.databinding.FragmentChooseUsersBinding
import ru.netology.nmedia.viewmodel.PostViewModel

@AndroidEntryPoint
class SelectPostUsersFragment : Fragment() {
    private val postViewModel: PostViewModel by activityViewModels()
    private val adapter = SelectUsersAdapter { user, isChecked ->
        user.isSelected = isChecked
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentChooseUsersBinding.inflate(inflater, container, false)

        binding.usersRecycleView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SelectPostUsersFragment.adapter
        }

        postViewModel.getUsers()

        postViewModel.allUsers.observe(viewLifecycleOwner) { users ->
            adapter.submitList(users)
        }

        binding.saveButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}