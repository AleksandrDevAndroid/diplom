package ru.netology.nmedia.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import ru.netology.nmedia.dto.Media

interface JobMediaService {
    @GET("my/jobs")
    suspend fun getJobs()

    @POST("my/jobs")
    suspend fun saveJob()

    @DELETE("my/jobs/{id}")
    suspend fun deleteJob(@Path("id") id: Long)

    @Multipart
    @POST("media")
    suspend fun upload(@Part file: MultipartBody.Part): Response<Media>

    @GET("{userId}/jobs")
    suspend fun getUserJobs(@Path("id") id: Long)

}