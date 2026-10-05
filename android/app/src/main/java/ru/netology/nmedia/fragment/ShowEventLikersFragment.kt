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
import ru.netology.nmedia.adapter.SelectedEventLikersAdapter
import ru.netology.nmedia.databinding.FragmentShowLikersEventBinding
import ru.netology.nmedia.viewmodel.EventViewModel

@AndroidEntryPoint
class ShowEventLikersFragment : Fragment() {
    private val eventView: EventViewModel by activityViewModels()
    private val adapter = SelectedEventLikersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentShowLikersEventBinding.inflate(layoutInflater, container, false)

        binding.usersRecycleView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ShowEventLikersFragment.adapter
        }


        eventView.selectEvent.observe(viewLifecycleOwner) { post ->
            post?.let { eventView.getLikers(it.id) }
        }

        eventView.likers.observe(viewLifecycleOwner) { users ->
            adapter.submitList(users)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }
}