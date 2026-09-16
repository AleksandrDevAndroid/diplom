package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Event
import ru.netology.nmedia.dto.EventType

@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val published: String,
    val datetime: Long,
    val type: EventType,
    val likedByMe: Boolean,
    val likeOwnerIds: List<Long> = emptyList(),
    val ownedByMe: Boolean,
    val link: String?,
    @Embedded
    val attachment: AttachmentEmbeddable?
) {
    fun toDto() = Event(
        id,
        authorId,
        author,
        authorAvatar,
        content,
        published,
        datetime,
        type,
        likedByMe,
        likeOwnerIds,
        ownedByMe,
        link,
        attachment?.toDto()
    )

    companion object {
        fun fromDto(dto: Event) =
            EventEntity(
                dto.id,
                dto.authorId,
                dto.author,
                dto.authorAvatar,
                dto.content,
                dto.published,
                dto.datetime,
                dto.type,
                dto.likedByMe,
                dto.likeOwnerIds,
                dto.ownedByMe,
                dto.link,
                AttachmentEmbeddable.fromDto(dto.attachment)
            )
    }
}

fun List<EventEntity>.toDto(): List<Event> = this.map { it.toDto() }
fun List<Event>.toEntity(): List<EventEntity> = this.map { EventEntity.fromDto(it) }

