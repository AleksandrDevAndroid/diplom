package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import ru.netology.nmedia.dto.Job
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.interfaceRepository.JobRepository
import javax.inject.Inject

private val empty = Job(
    id = 0,
    name = "",
    timeWork = "",
    position = "",
    webSite = "",
    ownerId = 0
)
@HiltViewModel
class JobViewModel @Inject constructor(
    private val repository: JobRepository,
    private val appAuth: AppAuth
) :
    ViewModel() {
    private val _job = MutableLiveData<List<Job>>()
    val job: LiveData<List<Job>> = _job

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error


    fun loadJobs() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val userId = appAuth.authState.value.id
                val jobs = repository.getJobs(userId)
                _job.value = jobs
            } catch (e: Exception) {
                _error.value = "${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun saveJob(job: Job) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val userId = appAuth.authState.value.id
                val ownerJob = job.copy(id = userId)
                repository.saveJob(ownerJob)
                loadJobs()
            } catch (e: Exception) {
                _error.value = "${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun deleteJob(job: Job) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.deleteJob(job)
                loadJobs()
            } catch (e: Exception) {
                _error.value = "${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}