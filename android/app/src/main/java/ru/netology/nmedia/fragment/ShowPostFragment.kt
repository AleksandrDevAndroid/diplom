package ru.netology.nmedia.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentShowPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.extensions.formatDate
import ru.netology.nmedia.view.loadCircleCrop
import ru.netology.nmedia.viewmodel.PostViewModel


class ShowPostFragment : Fragment() {
    private val viewModel: PostViewModel by activityViewModels()

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
                binding.attachment.isVisible = post.attachment != null

                val urlAttachment = post.attachment?.url
                if (!urlAttachment.isNullOrEmpty()) {
                    Glide.with(binding.attachment)
                        .load(urlAttachment)
                        .placeholder(R.drawable.outline_arrow_cool_down_24)
                        .override(1200, 800)
                        .centerCrop()
                        .error(R.drawable.error)
                        .into(binding.attachment)
                }
            }
        }

        viewModel.selectPost.observe(viewLifecycleOwner) { post ->
            post?.let { displayPost(it) }
        }
        viewModel.photo.observe(viewLifecycleOwner) {
            binding.attachment.setImageURI(it.uri)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root

    }

}
