package ru.netology.nmedia.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Users
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.PhotoModel
import ru.netology.nmedia.repository.interfaceRepository.PostRepository
import ru.netology.nmedia.repository.interfaceRepository.RegisterRepository
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.File
import javax.inject.Inject

private val empty = Post(
    id = 0,
    authorId = 0,
    author = "",
    authorAvatar = "",
    content = "",
    published = "",
    likedByMe = false,
    likes = 0,
    status = false,
    ownerByMe = false
)

private val noPhoto = PhotoModel()

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val registerRepository: RegisterRepository,
    appAuth: AppAuth
) :
    ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val data: Flow<PagingData<FeedItem>> = appAuth.authState
        .flatMapLatest { (myId, _) ->
            postRepository.data.map { posts ->
                posts.map { post ->
                    if (post is Post) {
                        post.copy(ownerByMe = myId == post.authorId)
                    } else {
                        post
                    }
                }
            }
        }.flowOn(Dispatchers.Default)
        .cachedIn(viewModelScope)

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val _edited = MutableLiveData(empty)
    val edited: LiveData<Post> = _edited
    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo

    private val _selectPost = MutableLiveData<Post>()
    val selectPost: LiveData<Post> = _selectPost

    val _selectUsers = MutableLiveData<List<Users>>()
    val selectUsers: LiveData<List<Users>> = _selectUsers

    val _allUsers = MutableLiveData<List<Users>>()
    val allUsers: LiveData<List<Users>> = _allUsers


    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _likers = MutableLiveData<List<Users>>()
    val likers: LiveData<List<Users>> = _likers

    private val _loadedPost = MutableLiveData<Post>()
    val loadedPost: LiveData<Post> = _loadedPost



    fun loadPost(postId: Long) {
        viewModelScope.launch {
            try {
                val post = postRepository.getPost(postId)
                _loadedPost.postValue(post)
            } catch (e: Exception) {
                _loadedPost.postValue(_selectPost.value)
            }
        }
    }
    fun refreshPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(refreshing = true)
            postRepository.updateStatus()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun getMentionedIds(): List<Long> {
        return _allUsers.value
            ?.filter { it.isSelected }
            ?.map { it.id }
            ?: emptyList()
    }

    fun save() {
        edited.value?.let { post ->
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    val mentionIds = getMentionedIds()
                    val postWithMentions = post.copy(mentionIds = mentionIds)
                    when (_photo.value) {
                        noPhoto -> postRepository.save(postWithMentions)
                        else -> _photo.value?.file?.let { file ->
                            postRepository.saveWithAttachment(postWithMentions, file)
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

    fun edit(post: Post) {
        _edited.value = post
    }

    fun selectPost(post: Post) {
        _selectPost.value = post
    }

    fun likeById(id: Long, likedByMe: Boolean) {
        viewModelScope.launch {
            try {
                if (!likedByMe) {
                    postRepository.likeById(id)
                } else postRepository.dislikeById(id)
            } catch (e: Exception) {
                FeedModelState(error = true)
            }
        }
    }

    fun removeById(id: Long) {
        viewModelScope.launch {
            try {
                postRepository.removeById(id)
            } catch (_: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun getNewPost() {
        viewModelScope.launch {
            val id = postRepository
        }
    }

    fun updateStatus() {
        viewModelScope.launch {
            postRepository.updateStatus()
        }
    }

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.postValue(PhotoModel(uri, file))
    }

    fun saveEdited() {
        val post = _edited.value ?: return
        val file = _photo.value?.file
        viewModelScope.launch {
            when {
                _photo.value != noPhoto && _photo.value != null ->
                    postRepository.saveWithAttachment(post, file)

                else -> postRepository.save(post)
            }
            _postCreated.value = Unit
            _edited.value = null
        }
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        _edited.value = edited.value?.copy(content = text)
    }

    fun getLikers(postId: Long) {
        viewModelScope.launch {
            try {
                val post = postRepository.getPost(postId)
                val likeOwnerId = post?.likeOwnerIds ?: emptyList()
                val usersMap = post?.users ?: emptyMap()
                val likers = likeOwnerId.mapNotNull { id ->
                    usersMap[id.toString()]?.let { info ->
                        Users(
                            id = id,
                            login = info.name,
                            name = info.name,
                            avatar = info.avatar
                        )
                    }
                }
                _likers.postValue(likers)
            } catch (e: Exception) {
                _likers.postValue(emptyList())
            }
        }
    }

    fun getUsers() {
        viewModelScope.launch {
            try {
                val allUsers = registerRepository.getUsers()
                _allUsers.postValue(allUsers)
            } catch (e: Exception) {
                _allUsers.postValue(emptyList())
            }
        }
    }
}


