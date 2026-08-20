package ru.netology.nmedia.api

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.netology.nmedia.dto.Post

interface MyWallService {
    @POST("my/wall/{id}/likes")
    suspend fun likesById(@Path("id") id: Long): Response<Post>

    @DELETE("my/wall/{id}/likes")
    suspend fun dislikesById(@Path("id") id: Long): Response<Unit>

    @GET("my/wall")
    suspend fun getMyWall(): Response<List<Post>>

    @GET("my/wall/{id}/newer")
    suspend fun getNewer(
        @Path("id")
        id: Long,
        @Query("count") count: Int
    ): Response<List<Post>>

    @GET("my/wall/{id}/before")
    suspend fun getBefore(
        @Path("id")
        id: Long,
        @Query("count") count: Int
    ): Response<List<Post>>

    @GET("my/wall/{id}/after")
    suspend fun getAfter(
        @Path("id")
        id: Long,
        @Query("count") count: Int
    ): Response<List<Post>>

    @GET("my/wall/{id}")
    suspend fun getPost(
        @Path("id")
        id: Long
    ): Response<Post>

    @GET("my/wall/latest")
    suspend fun getLatest(
        @Query("count") count: Int
    ): Response<List<Post>>

}