package ru.netology.nmedia.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.LikersAvatarAdapter
import ru.netology.nmedia.adapter.SelectedUsersAdapter
import ru.netology.nmedia.databinding.FragmentShowEventBinding
import ru.netology.nmedia.dto.Event
import ru.netology.nmedia.dto.UserInfo
import ru.netology.nmedia.dto.Users
import ru.netology.nmedia.extensions.formatDate
import ru.netology.nmedia.view.loadCircleCrop
import ru.netology.nmedia.viewmodel.EventViewModel

@AndroidEntryPoint
class ShowEventFragment : Fragment() {
    private val eventViewModel: EventViewModel by activityViewModels()

    private val speakersAdapter = SelectedUsersAdapter()
    private val likersAdapter = LikersAvatarAdapter()
    private val participantsAdapter = SelectedUsersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentShowEventBinding.inflate(inflater, container, false)

        fun mapIdsToUsers(ids: List<Long>, users: Map<String, UserInfo>?): List<Users> =
            ids.mapNotNull { id ->
                users?.get(id.toString())?.let { info ->
                    Users(id, info.name, info.name, info.avatar)
                }
            }

        fun displayEvent(event: Event) = with(binding) {
            author.text = event.author
            published.text = event.published.formatDate()
            content.text = event.content
            avatar.loadCircleCrop(event.authorAvatar)
            like.isChecked = event.likedByMe
            like.text = event.likeOwnerIds.size.toString()
            job.text = event.authorJob
            typeEvent.text = event.type
            participantsCount.text = event.participantsIds.size.toString()


            val urlAttachment = event.attachment?.url
            attachmentPhoto.isVisible = !urlAttachment.isNullOrEmpty()
            if (!urlAttachment.isNullOrEmpty()) {
                Glide.with(binding.attachmentPhoto)
                    .load(urlAttachment)
                    .placeholder(R.drawable.outline_arrow_cool_down_24)
                    .override(1200, 800)
                    .centerCrop()
                    .error(R.drawable.error)
                    .into(binding.attachmentPhoto)
            }
        }

        fun displaySpeakers(event: Event) {
            val speakers = mapIdsToUsers(event.speakerIds, event.users)
            speakersAdapter.submitList(speakers)
        }

        fun displayLikers(event: Event) {
            val likers = mapIdsToUsers(event.likeOwnerIds, event.users)
            likersAdapter.submitList(likers)
        }

        fun displayParticipants(event: Event) {
            val participants = mapIdsToUsers(event.participantsIds, event.users)
            binding.participantsCount.text = participants.size.toString()
            participantsAdapter.submitList(participants)
        }

        binding.speakersRecycler.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            adapter = speakersAdapter
        }
        binding.likersRecycler.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            adapter = likersAdapter
        }
        binding.participantsUsersAvatars.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            adapter = participantsAdapter
        }

        eventViewModel.selectEvent.observe(viewLifecycleOwner) { event ->
            event?.let { eventViewModel.loadEvent(it.id) }
        }

        eventViewModel.loadedEvent.observe(viewLifecycleOwner) { event ->
            event?.let {
                displayEvent(it)
                displaySpeakers(it)
                displayLikers(it)
                displayParticipants(it)
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}