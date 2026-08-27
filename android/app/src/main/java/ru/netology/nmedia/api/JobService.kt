    package ru.netology.nmedia.api

    import retrofit2.Response
    import retrofit2.http.Body
    import retrofit2.http.DELETE
    import retrofit2.http.GET
    import retrofit2.http.POST
    import retrofit2.http.Path
    import ru.netology.nmedia.dto.Job

    interface JobService {
        @GET("my/jobs")
        suspend fun getJobs(): Response<List<Job>>

        @POST("my/jobs")
        suspend fun saveJob(@Body job: Job): Response<Job>

        @DELETE("my/jobs/{id}")
        suspend fun deleteJob(@Path("id") id: Long): Response<Unit>

        @GET("{userId}/jobs")
        suspend fun getUserJobs(@Path("id") id: Long): Response<List<Job>>

    }