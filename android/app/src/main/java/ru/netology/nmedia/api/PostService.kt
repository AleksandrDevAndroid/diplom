package ru.netology.nmedia.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.netology.nmedia.dto.Post

interface PostService {
    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>

    @POST("posts")
    suspend fun savePost(@Body post: Post): Response<Post>

    @POST("posts/{id}/likes")
    suspend fun likesById(
        @Path("id")
        id: Long
    ): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun dislikesById(
        @Path("id")
        id: Long
    ): Response<Post>

    @GET("posts/{id}/newer")
    suspend fun getNewer(
        @Path("id")
        id: Long,
        @Query("count") count: Int
    ): Response<List<Post>>

    @GET("posts/{id}/before")
    suspend fun getBefore(
        @Path("id")
        id: Long,
        @Query("count") count: Int
    ): Response<List<Post>>

    @GET("posts/{id}/after")
    suspend fun getAfter(
        @Path("id")
        id: Long,
        @Query("count") count: Int
    ): Response<List<Post>>

    @GET("posts/{id}")
    suspend fun getPost(
        @Path("id")
        id: Long
    ): Response<Post>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Long): Response<Unit>

    @GET("posts/latest")
    suspend fun getLatest(
        @Query("count") count: Int
    ): Response<List<Post>>
}