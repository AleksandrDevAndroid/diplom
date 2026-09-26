package ru.netology.nmedia.fragment

import android.content.Intent
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
import ru.netology.nmedia.viewmodel.RegisterViewModel

@AndroidEntryPoint
class EventFeedFragment : Fragment() {

    private val eventViewModel: EventViewModel by activityViewModels()
    private val authViewModel : RegisterViewModel by activityViewModels()
    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Authentication Required")
            .setMessage("Please sign in to access this feature.")
            .setPositiveButton("Sign In") { dialog, _ ->
                findNavController().navigate(R.id.login_fragment)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedEventBinding.inflate(inflater, container, false)

        val adapter = EventAdapter(object : OnEventInteractionListener {
            override fun onLike(event: Event) {
                if (!authViewModel.authenticated) {
                    showDialog()
                    return
                }
                eventViewModel.likeById(event.id, event.likedByMe)
                eventViewModel.refreshEvent()
            }

            override fun onEdit(event: Event) {
                eventViewModel.edit(event)
                findNavController().navigate(R.id.action_event_feed_to_edit_event)
            }

            override fun onRemove(event: Event) {
                eventViewModel.removeById(event.id)
                eventViewModel.refreshEvent()
            }

            override fun onShare(event: Event) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, event.content)
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(
                        intent,
                        getString(ru.netology.nmedia.R.string.chooser_share_post)
                    )
                startActivity(shareIntent)
            }
        })

        binding.listEvent.adapter = adapter

        eventViewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.loading
            binding.swiperefresh.isRefreshing = state.refreshing

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
