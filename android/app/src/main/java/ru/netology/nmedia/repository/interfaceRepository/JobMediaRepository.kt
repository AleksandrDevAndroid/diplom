package ru.netology.nmedia.repository.interfaceRepository

interface JobMediaRepository {
    suspend fun getJobs()
    suspend fun saveJobs()
    suspend fun deleteJob()
    suspend fun getUserJobs()

}