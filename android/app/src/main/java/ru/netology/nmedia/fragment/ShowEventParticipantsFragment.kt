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
import ru.netology.nmedia.adapter.SelectedParticipantsAdapter
import ru.netology.nmedia.databinding.FragmentShowLikersEventBinding
import ru.netology.nmedia.viewmodel.EventViewModel
import kotlin.getValue

@AndroidEntryPoint
class ShowEventParticipantsFragment : Fragment() {
    private val eventView: EventViewModel by activityViewModels()
    private val adapter = SelectedParticipantsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentShowLikersEventBinding.inflate(layoutInflater, container, false)

        binding.usersRecycleView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ShowEventParticipantsFragment.adapter
        }


        eventView.selectEvent.observe(viewLifecycleOwner) { post ->
            post?.let { eventView.getParticipants(it.id) }
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