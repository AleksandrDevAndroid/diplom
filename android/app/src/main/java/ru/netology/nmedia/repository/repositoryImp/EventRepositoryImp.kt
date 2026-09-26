package ru.netology.nmedia.repository.repositoryImp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.nmedia.api.EventsService
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Event
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Users
import ru.netology.nmedia.enum.AttachmentType
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import ru.netology.nmedia.repository.interfaceRepository.EventRepository
import java.io.File
import java.io.IOException
import javax.inject.Inject

class EventRepositoryImp @Inject constructor(
    private val apiService: EventsService,
) : EventRepository {

    private val _data = MutableStateFlow<List<Event>>(emptyList())
    override val data: Flow<List<Event>> = _data

    override suspend fun getById(id: Long): Event {
        try {
            val response = apiService.getEvent(id)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun save(event: Event) {
        try {
            val response = apiService.createEvent(event)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            refresh()
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun removeById(id: Long) {
        try {
            val response = apiService.deleteEvent(id)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun likeById(id: Long): Event {
        try {
            val response = apiService.likeEvent(id)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun dislikeById(id: Long): Event {
        try {
            val response = apiService.dislikeEvent(id)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun getParticipants(eventId: Long): List<Users> {
        try {
            val response = apiService.getParticipants(eventId)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            return response.body() ?: emptyList()
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveWithAttachment(event: Event, file: File?) {
        val media = upload(file ?: throw UnknownError)
        val copyEvent = event.copy(attachment = Attachment(media.url, AttachmentType.IMAGE))
        save(copyEvent)
    }

    private suspend fun upload(file: File): Media {
        try {
            val part = MultipartBody.Part.createFormData(
                "file",
                file.name,
                file.asRequestBody()
            )
            val response = apiService.upload(part)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun refresh() {
        try {
            val response = apiService.getEvents()
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())
            _data.value = response.body().orEmpty()
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun getNewerEvent() { /* TODO */
    }

    override suspend fun getBeforeEvent() { /* TODO */
    }

    override suspend fun getAfterEvent() { /* TODO */
    }

    override suspend fun getLatestEvent() { /* TODO */
    }
}