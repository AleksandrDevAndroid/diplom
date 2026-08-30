package ru.netology.nmedia.repository.interfaceRepository

import ru.netology.nmedia.dto.Users
import java.io.File

interface UserRepository {
    suspend fun signIn(login: String, pass: String)
    suspend fun signUp(login: String, pass: String, name: String, media: File)
    suspend fun getUsers() : List<Users>
    suspend fun getUser(userId : Long) : Users
}