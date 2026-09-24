package ru.netology.nmedia.repository.repositoryImp

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.nmedia.api.EventsService
import ru.netology.nmedia.dao.EventDao
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Event
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.Users
import ru.netology.nmedia.entity.EventEntity
import ru.netology.nmedia.enum.AttachmentType
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import ru.netology.nmedia.repository.interfaceRepository.EventRepository
import java.io.File
import java.io.IOException
import javax.inject.Inject

class EventRepositoryImp @Inject constructor(
    private val dao: EventDao,
    private val apiService: EventsService,
) : EventRepository {

    override val data: Flow<List<Event>> = dao.getAll().map { entities ->
        entities.map { it.toDto() }
    }

    override suspend fun getAll() {
        try {
            val response = apiService.getEvents()
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.clear()
            dao.insert(body.map { EventEntity.fromDto(it) })
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun getById(id: Long): Event {
        return dao.getId(id).toDto()
    }

    override suspend fun save(event: Event) {
        try {
            val response = apiService.createEvent(event)
            if (!response.isSuccessful) throw ApiError(response.code(), response.message())

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(EventEntity.fromDto(body))
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
            dao.removeById(id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun likeById(id: Long): Event {
        try {
            val response = apiService.likeEvent(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(EventEntity.fromDto(body))
            return body
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun dislikeById(id: Long): Event {
        try {
            val response = apiService.dislikeEvent(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(EventEntity.fromDto(body))
            return body
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

    override suspend fun saveWithAttachment(
        event: Event,
        file: File?
    ) {
        val media = upload(file)
        val copyPost = event.copy(attachment = Attachment(media.url, AttachmentType.IMAGE))
        save(copyPost)
    }

    private suspend fun upload(file: File?): Media {
        try {
            val part = MultipartBody.Part.createFormData(
                "file", file?.name, (file?.asRequestBody() ?: "") as RequestBody
            )
            val response = apiService.upload(part)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
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

    override suspend fun getLatestEvent() {
        TODO("Not yet implemented")
    }
}
