package ru.netology.nmedia.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import ru.netology.nmedia.dto.Media
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
    @Multipart
    @POST("media")
    suspend fun upload(@Part file: MultipartBody.Part): Response<Media>
}