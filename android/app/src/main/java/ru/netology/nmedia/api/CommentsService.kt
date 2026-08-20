package ru.netology.nmedia.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.nmedia.dto.Comment

interface CommentsService {
    @GET("posts/{postId}/comments")
    suspend fun getComments(@Path("postId") postId: Long): Response<List<Comment>>

    @POST("posts/{postId}/comments")
    suspend fun saveComment(
        @Path("postId") postId: Long,
        @Body comment: Comment
    ): Response<Comment>

    @POST("posts/{postId}/comments/{id}/likes")
    suspend fun likeComment(
        @Path("postId") postId: Long,
        @Path("id") id: Long
    ): Response<Comment>

    @DELETE("posts/{postId}/comments/{id}/likes")
    suspend fun dislikeComment(
        @Path("postId") postId: Long,
        @Path("id") id: Long
    ): Response<List<Comment>>

    @DELETE("posts/{postId}/comments/{id}")
    suspend fun deleteComment(
        @Path("postId") postId: Long,
        @Path("id") id: Long
    ): Response<Unit>
}