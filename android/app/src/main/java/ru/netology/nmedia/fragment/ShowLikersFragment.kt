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
import ru.netology.nmedia.adapter.LikersAdapter
import ru.netology.nmedia.databinding.FragmentShowLikersBinding
import ru.netology.nmedia.viewmodel.PostViewModel

@AndroidEntryPoint
class ShowLikersFragment() : Fragment() {
    private val postViewModel: PostViewModel by activityViewModels()
    private val adapter = LikersAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentShowLikersBinding.inflate(inflater, container, false)


        binding.usersRecycleView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ShowLikersFragment.adapter
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