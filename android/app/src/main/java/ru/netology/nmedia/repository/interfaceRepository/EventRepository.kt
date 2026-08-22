package ru.netology.nmedia.repository.interfaceRepository

interface EventRepository {
    suspend fun getEvents()
    suspend fun createEven()
    suspend fun getParticipants()
    suspend fun deleteParticipants()
    suspend fun likeEvent()
    suspend fun dislikeEvent()
    suspend fun getNewerEvent()
    suspend fun getBeforeEvent()
    suspend fun getAfterEvent()
    suspend fun getEvent()
    suspend fun deleteEvent()
    suspend fun getLatestEvent()

}