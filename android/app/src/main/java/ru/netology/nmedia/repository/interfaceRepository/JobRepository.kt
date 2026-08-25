package ru.netology.nmedia.repository.interfaceRepository
import ru.netology.nmedia.dto.Job

interface JobRepository {
    suspend fun getJobs(ownerId: Long): List<Job>

    suspend fun saveJob(job: Job): Job

    suspend fun deleteJob(job: Job)

}