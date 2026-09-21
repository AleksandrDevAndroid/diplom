package ru.netology.nmedia.adapter

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.EventCardBinding
import ru.netology.nmedia.dto.Event
import ru.netology.nmedia.extensions.formatDate
import ru.netology.nmedia.view.loadCircleCrop

interface OnEventInteractionListener {
    fun onLike(event: Event) {}
    fun onEdit(event: Event) {}
    fun onRemove(event: Event) {}
    fun onShare(event: Event) {}
}

class EventViewHolder(
    private val binding: EventCardBinding,
    private val onEventInteractionListener: OnEventInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SuspiciousIndentation")
    fun bind(event: Event) {
        binding.apply {
            author.text = event.author
            published.text = event.published.formatDate()
            content.text = event.content
            avatar.loadCircleCrop(event.authorAvatar)
            like.isChecked = event.likedByMe
            like.text = "${event.likes}"
            dateTime.text = event.datetime.formatDate()
            typeEvent.text = event.type
            attachmentPhoto.isVisible = false


            val urlAttachment = event.attachment?.url
            menu.isVisible = event.ownedByMe

            if (!event.attachment?.url.isNullOrEmpty()) {
                attachmentPhoto.isVisible = true
                Glide.with(binding.attachmentPhoto)
                    .load(urlAttachment)
                    .placeholder(R.drawable.outline_arrow_cool_down_24)
                    .override(1200, 800)
                    .centerCrop()
                    .timeout(6_000)
                    .error(R.drawable.error)
                    .into(binding.attachmentPhoto)
            }


            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onEventInteractionListener.onRemove(event)
                                true
                            }

                            R.id.edit -> {
                                onEventInteractionListener.onEdit(event)
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
                    interpolator = BounceInterpolator()
                }.start()
                onEventInteractionListener.onLike(event)
            }
            share.setOnClickListener {
                onEventInteractionListener.onShare(event)
            }
        }
    }
}

class EventAdapter(
    private val onEventInteractionListener: OnEventInteractionListener,
) : RecyclerView.Adapter<EventViewHolder>() {

    private var events = listOf<Event>()

    fun submitList(event: List<Event>) {
        events = event
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {
        val binding = EventCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding, onEventInteractionListener)
    }

    override fun onBindViewHolder(
        holder: EventViewHolder,
        position: Int
    ) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int {
        return events.size
    }
}