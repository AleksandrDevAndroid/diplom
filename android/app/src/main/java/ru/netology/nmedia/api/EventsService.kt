package ru.netology.nmedia.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.netology.nmedia.dto.Event
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Users

interface EventsService {
    @GET("events")
    suspend fun getEvents(): Response<List<Event>>

    @POST("events")
    suspend fun createEvent(@Body event: Event): Response<Event>

    @GET("events/{id}/participants")
    suspend fun getParticipants(@Path("id") id: Long): Response<Event>

    @DELETE("events/{id}/participants")
    suspend fun deleteParticipants(@Path("id") id: Long): Response<Post>

    @POST("events/{id}/likes")
    suspend fun likeEvent(@Path("id") id: Long): Response<Event>

    @DELETE("events/{id}/likes")
    suspend fun dislikeEvent(@Path("id") id: Long): Response<Event>

    @GET("events/{id}/newer")
    suspend fun getNewerEvent(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<Event>>

    @GET("events/{id}/before")
    suspend fun getBeforeEvent(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<Event>>

    @GET("events/{id}/after")
    suspend fun getAfterEvent(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<Event>>

    @GET("events/{id}")
    suspend fun getEvent(@Path("id") id : Long) : Response<Unit>

    @DELETE("events/{id}")
    suspend fun deleteEvent(@Path("id") id : Long) : Response<Event>

    @GET("events/latest")
    suspend fun getLatestEvent(
        @Query("count") count: Int
    ): Response<List<Event>>

}