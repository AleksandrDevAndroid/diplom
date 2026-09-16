package ru.netology.nmedia.fragment

import android.os.Bundle
import ru.netology.nmedia.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.databinding.FragmentFeedEventBinding
@AndroidEntryPoint
class EventFeedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedEventBinding.inflate(inflater,container,false)

        binding.tabPosts.setOnClickListener {
            findNavController().navigate(R.id.action_event_feed_to_feedFragment5)
        }
        binding.tabUsers.setOnClickListener {
            findNavController().navigate(R.id.action_event_feed_to_show_users)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_event_feed_to_new_event)
        }
        return binding.root
    }
}