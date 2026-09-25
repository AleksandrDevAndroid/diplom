package ru.netology.nmedia.repository.interfaceRepository

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Event
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Users
import java.io.File

interface EventRepository {
    val data: Flow<List<Event>>

    suspend fun refresh()

    suspend fun getById(id: Long): Event
    suspend fun save(event: Event)

    suspend fun removeById(id: Long)

    suspend fun likeById(id: Long): Event

    suspend fun dislikeById(id: Long): Event
    suspend fun getParticipants(eventId: Long): List<Users>
    suspend fun saveWithAttachment(event: Event, file: File?)

    suspend fun getNewerEvent()
    suspend fun getBeforeEvent()
    suspend fun getAfterEvent()
    suspend fun getLatestEvent()

}