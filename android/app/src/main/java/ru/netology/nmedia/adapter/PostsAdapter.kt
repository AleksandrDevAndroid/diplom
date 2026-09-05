package ru.netology.nmedia.adapter

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.DateSeparator
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.extensions.formatDate
import ru.netology.nmedia.view.loadCircleCrop

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onShare(post: Post) {}
    fun onOpen(post: Post) {}
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener,
) : PagingDataAdapter<FeedItem, RecyclerView.ViewHolder>(PostDiffCallback()) {

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is Post -> R.layout.card_post
            null -> error("unknow type item")
            is DateSeparator -> R.layout.item_date
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.card_post -> {
                val binding =
                    CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PostViewHolder(binding, onInteractionListener)
            }
            R.layout.item_date -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_date, parent, false)
                DateSeparatorViewHolder(view)
            }
            else -> error("unknow type item $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is Post -> (holder as? PostViewHolder)?.bind(item)
            null -> error("unknow type item ${getItem(position)}")
            is DateSeparator -> (holder as? DateSeparatorViewHolder)?.bind(item.text)
        }
    }

}
class DateSeparatorViewHolder(view:View) : RecyclerView.ViewHolder(view) {
    private val textView: android.widget.TextView = view.findViewById(R.id.date_text)

    fun bind(text: String) {
        textView.text = text
    }
}


class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
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

            menu.isVisible = post.ownerByMe
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            like.setOnClickListener {
                val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1F, 1.25F, 1F)
                val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1F, 1.25F, 1F)
                    ObjectAnimator.ofPropertyValuesHolder(it, scaleX, scaleY).apply {
                        duration = 500
                        repeatCount = 100
                        interpolator = BounceInterpolator()
                    }.start()
                onInteractionListener.onLike(post)
            }

            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            attachment.setOnClickListener {
                onInteractionListener.onOpen(post)
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<FeedItem>() {
    override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        return oldItem == newItem
    }
}
