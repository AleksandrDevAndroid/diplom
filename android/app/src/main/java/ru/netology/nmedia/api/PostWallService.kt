package ru.netology.nmedia.api

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.netology.nmedia.dto.Post

interface PostWallService {

    @POST("{authorId}/wall/{id}/likes")
    suspend fun likesById(
        @Path("authorId")
        authorId: Long,
        @Path("id")
        id: Long
    ): Response<Post>

    @DELETE("{authorId}/wall/{id}/likes")
    suspend fun dislikesById(
        @Path("authorId")
        authorId: Long,
        @Path("id")
        id: Long
    ): Response<Unit>

    @GET("{authorId}/wall")
    suspend fun postAuthor(@Path("authorId") authorId: Long): Response<List<Post>>

    @GET("{authorId}/wall/{id}/newer")
    suspend fun getNewer(
        @Path("authorId")
        authorId: Long,
        @Path("id")
        id: Long,
        @Query("count") count: Int
    ): Response<List<Post>>

    @GET("{authorId}/wall/{id}/before")
    suspend fun getBefore(
        @Path("authorId")
        authorId: Long,
        @Path("id")
        id: Long,
        @Query("count") count: Int
    ): Response<List<Post>>

    @GET("{authorId}/wall/{id}/after")
    suspend fun getAfter(
        @Path("authorId")
        authorId: Long,
        @Path("id")
        id: Long,
        @Query("count") count: Int
    ): Response<List<Post>>

    @GET("{authorId}/wall/{id}")
    suspend fun getPost(
        @Path("authorId")
        authorId: Long,
        @Path("id")
        id: Long
    ): Response<Post>

    @GET("{authorId}/wall/latest")
    suspend fun getLatest(
        @Path("authorId")
        authorId: Long,
        @Query("count") count: Int
    ): Response<List<Post>>
}