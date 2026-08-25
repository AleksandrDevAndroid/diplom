package ru.netology.nmedia.repository.repositoryImp

import ru.netology.nmedia.api.JobService
import ru.netology.nmedia.dao.JobDao
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Job
import ru.netology.nmedia.entity.JobEntity
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.UnknownError
import ru.netology.nmedia.repository.interfaceRepository.JobRepository
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobRepositoryImp @Inject constructor(
    val dao: JobDao, val apiService: JobService, val appDb: AppDb
) : JobRepository {

    override suspend fun getJobs(ownerId: Long): List<Job> {
        try {
            val response = apiService.getJobs(ownerId)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            val entity = body.map { JobEntity.fromDto(it) }
            dao.insertAll(entity)

        } catch (e: IOException) {
            throw UnknownError

        } catch (e: Exception) {
            throw UnknownError
        }
        return emptyList()
    }

    override suspend fun saveJob(job: Job): Job {
        return try {
            val response = apiService.saveJob(job)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            val entity = JobEntity.fromDto(body)
            dao.insert(entity)
            body
        } catch (e: IOException) {
            throw UnknownError

        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun deleteJob(job: Job) {
        try {
            val response = apiService.deleteJob(job.id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val entity = JobEntity.fromDto(job)
            dao.delete(entity)
        } catch (e: IOException) {
            throw UnknownError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}