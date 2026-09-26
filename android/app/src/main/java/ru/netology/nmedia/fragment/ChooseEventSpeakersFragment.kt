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
import ru.netology.nmedia.adapter.ChooseUsersAdapter
import ru.netology.nmedia.databinding.FragmentChooseUsersBinding
import ru.netology.nmedia.viewmodel.EventViewModel

@AndroidEntryPoint
class ChooseEventSpeakersFragment : Fragment() {
    private val eventViewModel: EventViewModel by activityViewModels()
    private val adapter = ChooseUsersAdapter { user, isChecked ->
        user.isSelected = isChecked
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentChooseUsersBinding.inflate(inflater, container, false)

        binding.usersRecycleView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ChooseEventSpeakersFragment.adapter
        }

        eventViewModel.getSpeakers()

        eventViewModel.speakers.observe(viewLifecycleOwner) { speaker ->
            adapter.submitList(speaker)
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