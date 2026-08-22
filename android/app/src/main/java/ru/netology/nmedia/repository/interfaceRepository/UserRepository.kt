package ru.netology.nmedia.repository.interfaceRepository

import java.io.File

interface UserRepository {
    suspend fun signIn(login: String, pass: String)
    suspend fun signUp(login: String, pass: String, name: String, media: File)
    suspend fun getUsers()
    suspend fun getUser()
}