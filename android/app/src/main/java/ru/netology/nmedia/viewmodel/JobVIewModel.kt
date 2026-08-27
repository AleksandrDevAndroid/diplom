package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.Job
import ru.netology.nmedia.repository.interfaceRepository.JobRepository
import javax.inject.Inject

private val empty = Job(
    id = 0,
    name = "",
    position = "",
    start = "",
    finish = "",
    link = "",
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
                val ownerJob = job.copy(ownerId = userId)
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