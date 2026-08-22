package ru.netology.nmedia.repository.interfaceRepository

interface CommentsRepository {
    suspend fun getComments()
    suspend fun saveComment()
    suspend fun likeComment()
    suspend fun dislikeComment()
    suspend fun deleteComment()
}