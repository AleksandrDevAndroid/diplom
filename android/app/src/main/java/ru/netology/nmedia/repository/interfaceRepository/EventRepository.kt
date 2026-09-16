package ru.netology.nmedia.repository.interfaceRepository

import ru.netology.nmedia.dto.Event
import ru.netology.nmedia.dto.Users

interface EventRepository {
    suspend fun getEvents(): List<Event>
    suspend fun createEven()
    suspend fun getParticipants(): List<Users>
    suspend fun deleteParticipants()
    suspend fun likeEvent(id: Event)
    suspend fun dislikeEvent(id: Event)
    suspend fun getNewerEvent()
    suspend fun getBeforeEvent()
    suspend fun getAfterEvent()
    suspend fun getEvent(id: Event)
    suspend fun deleteEvent(id: Event)
    suspend fun getLatestEvent()

}