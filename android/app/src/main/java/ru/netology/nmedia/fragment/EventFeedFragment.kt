package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.EventAdapter
import ru.netology.nmedia.adapter.OnEventInteractionListener
import ru.netology.nmedia.databinding.FragmentFeedEventBinding
import ru.netology.nmedia.dto.Event
import ru.netology.nmedia.viewmodel.EventViewModel

@AndroidEntryPoint
class EventFeedFragment : Fragment() {

    private val eventViewModel: EventViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedEventBinding.inflate(inflater, container, false)

        val adapter = EventAdapter(object : OnEventInteractionListener {
            override fun onLike(event: Event) {
                eventViewModel.likeById(event.id, event.likedByMe)
            }

            override fun onEdit(event: Event) {
                eventViewModel.edit(event)
                findNavController().navigate(R.id.action_event_feed_to_new_event)
            }

            override fun onRemove(event: Event) {
                eventViewModel.removeById(event.id)
            }

            override fun onShare(event: Event) {
            }
        })

        binding.listEvent.adapter = adapter

        eventViewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.loading

            if (state.error) {
                Snackbar.make(
                    binding.root,
                    ru.netology.nmedia.R.string.error_loading,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(ru.netology.nmedia.R.string.retry_loading) { eventViewModel.refreshEvent() }
                    .show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                eventViewModel.data.collectLatest { events ->
                    adapter.submitList(events)
                }
            }
        }

        binding.swiperefresh.setOnRefreshListener {
            eventViewModel.refreshEvent()
        }

        binding.tabPosts.setOnClickListener {
            findNavController().navigate(R.id.action_event_feed_to_feedFragment5)
        }
        binding.tabUsers.setOnClickListener {
            findNavController().navigate(R.id.action_event_feed_to_show_users)
        }
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_event_feed_to_new_event)
        }

        eventViewModel.refreshEvent()

        return binding.root
    }
}
