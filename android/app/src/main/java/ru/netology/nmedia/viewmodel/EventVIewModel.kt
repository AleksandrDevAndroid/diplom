package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Event
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Users
import ru.netology.nmedia.repository.interfaceRepository.EventRepository
import javax.inject.Inject

class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
): EventRepository {

    private val _events = MutableLiveData<Event>()
    val event: LiveData<Event> = _events

    override suspend fun getEvents(): List<Event> {
        TODO("Not yet implemented")
    }

    override suspend fun createEven() {
        TODO("Not yet implemented")
    }

    override suspend fun getParticipants(): List<Users> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteParticipants() {
        TODO("Not yet implemented")
    }

    override suspend fun likeEvent(id: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun dislikeEvent(id: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun getNewerEvent() {
        TODO("Not yet implemented")
    }

    override suspend fun getBeforeEvent() {
        TODO("Not yet implemented")
    }

    override suspend fun getAfterEvent() {
        TODO("Not yet implemented")
    }

    override suspend fun getEvent(id: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEvent(id: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun getLatestEvent() {
        TODO("Not yet implemented")
    }
}