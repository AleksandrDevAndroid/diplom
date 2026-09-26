package ru.netology.nmedia.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.Event
import ru.netology.nmedia.dto.Users
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.PhotoModel
import ru.netology.nmedia.repository.interfaceRepository.EventRepository
import ru.netology.nmedia.repository.interfaceRepository.RegisterRepository
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.File
import javax.inject.Inject

private val empty = Event(
    id = 0,
    authorId = 0,
    author = "",
    authorAvatar = "",
    content = "",
    published = "",
    datetime = "",
    type = "",
    likedByMe = false,
    ownedByMe = false,
    link = "",
    authorJob = "",
    participatedByMe = false,
    attachment = null,
)

private val noPhoto = PhotoModel()

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val registerRepository: RegisterRepository,
    appAuth: AppAuth
) :
    ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val data: Flow<List<Event>> = appAuth.authState
        .flatMapLatest { (myId, _) ->
            eventRepository.data.map { events ->
                events.map { event ->
                    event.copy(ownedByMe = myId == event.authorId)
                }
            }
        }.flowOn(Dispatchers.Default)

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val _edited = MutableLiveData(empty)
    val edited: LiveData<Event> = _edited

    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo

    private val _selectEvent = MutableLiveData<Event>()
    val selectEvent: LiveData<Event> = _selectEvent

    private val _selectUsers = MutableLiveData<List<Users>>()
    val selectUsers: LiveData<List<Users>> = _selectUsers

    private val _speakers = MutableLiveData<List<Users>>()
    val speakers: LiveData<List<Users>> = _speakers

    private val _eventCreated = SingleLiveEvent<Unit>()
    val eventCreated: LiveData<Unit>
        get() = _eventCreated

    private val _loadedEvent = MutableLiveData<Event>()
    val loadedEvent: LiveData<Event> = _loadedEvent

    fun refreshEvent() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(refreshing = true)
            eventRepository.refresh()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun getSpeakersIds(): List<Long> {
        return _speakers.value
            ?.filter { it.isSelected }
            ?.map { it.id }
            ?: emptyList()
    }

    fun save() {
        _edited.value?.let { event ->
            _eventCreated.value = Unit
            viewModelScope.launch {
                try {
                    val speakersId = getSpeakersIds()
                    val eventWithParticipant = event.copy(speakerIds = speakersId)
                    when (_photo.value) {
                        noPhoto -> eventRepository.save(eventWithParticipant)
                        else -> _photo.value?.file?.let { file ->
                            eventRepository.saveWithAttachment(eventWithParticipant, file)
                        }
                    }
                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
            }
        }
        _edited.value = empty
        _photo.value = noPhoto
        _selectUsers.value = emptyList()
    }


    fun edit(event: Event) {
        _edited.value = event
    }


    fun likeById(id: Long, likedByMe: Boolean) {
        viewModelScope.launch {
            try {
                if (!likedByMe) {
                    eventRepository.likeById(id)
                } else eventRepository.dislikeById(id)
            } catch (e: Exception) {
                FeedModelState(error = true)
            }
        }
    }

    fun removeById(id: Long) {
        viewModelScope.launch {
            try {
                eventRepository.removeById(id)
            } catch (_: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.postValue(PhotoModel(uri, file))
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        _edited.value = edited.value?.copy(content = text)
    }

    fun saveEdited() {
        val post = _edited.value ?: return
        val file = _photo.value?.file
        viewModelScope.launch {
            when {
                _photo.value != noPhoto && _photo.value != null ->
                    eventRepository.saveWithAttachment(post, file)

                else -> eventRepository.save(post)
            }
            _eventCreated.value = Unit
            _edited.value = null
        }
    }

    fun changeDatetime(datetime: String) {
        _edited.value = _edited.value?.copy(datetime = datetime)
    }

    fun changeType(type: String) {
        _edited.value = _edited.value?.copy(type = type)
    }

    fun selectEvent(event: Event) {
        _selectEvent.value = event
    }

    fun loadEvent(eventId: Long) {
        viewModelScope.launch {
            try {
                val post = eventRepository.getById(eventId)
                _loadedEvent.postValue(post)
            } catch (e: Exception) {
                _loadedEvent.postValue(_selectEvent.value)
            }
        }
    }
    fun getSpeakers() {
        viewModelScope.launch {
            try {
                val allUsers = registerRepository.getUsers()
                _speakers.postValue(allUsers)
            } catch (e: Exception) {
                _speakers.postValue(emptyList())
            }
        }
    }
}



