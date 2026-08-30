package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.Users
import ru.netology.nmedia.model.FeedModelAuth
import ru.netology.nmedia.repository.interfaceRepository.UserRepository
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: UserRepository,
    appAuth: AppAuth
) :
    ViewModel() {
    val data = appAuth.authState.asLiveData()
    private val _user = MutableLiveData<Users>()
    val user: LiveData<Users> = _user
    private val _users = MutableLiveData<List<Users>>()
    val users: LiveData<List<Users>> = _users


    val authenticated: Boolean
        get() = !data.value?.token.isNullOrEmpty()


    private val _state = MutableLiveData<FeedModelAuth>()
    val dataState: LiveData<FeedModelAuth>
        get() = _state

    fun signUp(login: String, pass: String, name: String, media: File) {
        data.value.let {
            viewModelScope.launch {
                try {
                    repository.signUp(login, pass, name, media)
                    _state.value = FeedModelAuth(successes = true)
                } catch (_: Exception) {
                    _state.value = FeedModelAuth(error = true)
                }
            }
        }
    }

    fun getUsers() {
        viewModelScope.launch {
            try {
                val usersData = repository.getUsers()
                _users.value = usersData
            } catch (e: Exception) {
                "${e.message}"
            }
        }
    }

    fun getUser(userId: Long) {
        viewModelScope.launch {
            try {
                val userData = repository.getUser(userId)
                _user.value = userData
            } catch (e: Exception) {
                "${e.message}"
            }
        }
    }

}