package ru.netology.nmedia.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.loader.app.LoaderManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.LikersAvatarAdapter
import ru.netology.nmedia.adapter.UsersAdapter
import ru.netology.nmedia.databinding.FragmentShowPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.extensions.formatDate
import ru.netology.nmedia.view.loadCircleCrop
import ru.netology.nmedia.viewmodel.PostViewModel

@AndroidEntryPoint
class ShowPostFragment : Fragment() {
    private val postViewModel: PostViewModel by activityViewModels()
    private val userAdapter = UsersAdapter()
    private val likersAdapter = LikersAvatarAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentShowPostBinding.inflate(inflater, container, false)

        fun displayPost(post: Post) {
            with(binding) {
                author.text = post.author
                published.text = post.published.formatDate()
                content.text = post.content
                avatar.loadCircleCrop(post.authorAvatar)
                like.isChecked = post.likedByMe
                like.text = "${post.likes}"
                binding.attachmentPhoto.isVisible = post.attachment != null

                val urlAttachment = post.attachment?.url
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
        }

        binding.likersRecycler.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = likersAdapter
        }

        postViewModel.selectPost.observe(viewLifecycleOwner) { post ->
            post?.let {
                displayPost(it)
                postViewModel.getLikers(it.id)
            }
        }

        postViewModel.likers.observe(viewLifecycleOwner) { users ->
            likersAdapter.submitList(users)
        }

        postViewModel.photo.observe(viewLifecycleOwner) {
            binding.attachmentPhoto.setImageURI(it.uri)
        }

        postViewModel.likers.observe(viewLifecycleOwner) { users ->
            userAdapter.submitList(users)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.showUsers.setOnClickListener {
            findNavController().navigate(R.id.action_showPostFragment_to_show_users)
        }

        return binding.root
    }

}
