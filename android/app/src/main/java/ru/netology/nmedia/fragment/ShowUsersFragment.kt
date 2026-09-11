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
import ru.netology.nmedia.adapter.UsersAdapter
import ru.netology.nmedia.databinding.FragmentShowUsersBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.viewmodel.RegisterViewModel

@AndroidEntryPoint
class ShowUsersFragment() : Fragment() {
    private val postViewModel: PostViewModel by activityViewModels()
    private val adapter = UsersAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentShowUsersBinding.inflate(inflater, container, false)


        binding.usersRecycleView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ShowUsersFragment.adapter
        }


        postViewModel.selectPost.observe(viewLifecycleOwner) { post ->
            post?.let { postViewModel.getLikers(it.id) }
        }

        postViewModel.likers.observe(viewLifecycleOwner) { users ->
            adapter.submitList(users)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}