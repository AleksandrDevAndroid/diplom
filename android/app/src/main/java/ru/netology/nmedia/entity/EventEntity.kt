package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Event

@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorJob: String?,
    val authorAvatar: String?,
    val content: String,
    val datetime: String,
    val published: String,
    val typeEvent: String,
    val likedByMe: Boolean,
    val participatedByMe: Boolean,
    val link: String?,
    val ownedByMe: Boolean = false,
    val likes : Int,
    @Embedded
    val attachment: AttachmentEmbeddable?
) {
    fun toDto() = Event(
        id,
        authorId,
        author,
        authorJob,
        authorAvatar,
        content,
        datetime,
        published,
        typeEvent,
        likedByMe,
        participatedByMe,
        attachment?.toDto(),
        link,
        ownedByMe,
        likes
    )

    companion object {
        fun fromDto(dto: Event) =
            EventEntity(
                dto.id,
                dto.authorId,
                dto.author,
                dto.authorJob,
                dto.authorAvatar,
                dto.content,
                dto.datetime,
                dto.published,
                dto.type,
                dto.likedByMe,
                dto.participatedByMe,
                dto.link,
                dto.ownedByMe,
                dto.likes,
                AttachmentEmbeddable.fromDto(dto.attachment)
            )
    }
}

fun List<EventEntity>.toDto(): List<Event> = this.map { it.toDto() }
fun List<Event>.toEntity(): List<EventEntity> = this.map { EventEntity.fromDto(it) }

