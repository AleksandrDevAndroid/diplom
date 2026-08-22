package ru.netology.nmedia.repository.interfaceRepository

interface MyWallRepository {
    suspend fun likeById()
    suspend fun dislikeById()
    suspend fun getMyWall()
    suspend fun getNewer()
    suspend fun getBefore()
    suspend fun getAfter()
    suspend fun getPost()
    suspend fun getLastest()
}