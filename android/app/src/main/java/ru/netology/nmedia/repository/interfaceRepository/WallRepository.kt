package ru.netology.nmedia.repository.interfaceRepository

interface WallRepository {
    suspend fun likeById(id : Long)
    suspend fun dislikeById(id : Long)
    suspend fun postAuthor(id : Long)
    suspend fun getNewer()
    suspend fun getBefore()
    suspend fun getAfter()
    suspend fun getPost()
    suspend fun getLastest()
}