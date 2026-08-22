package ru.netology.nmedia.repository.repositoriImp

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.api.UserService
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import ru.netology.nmedia.repository.interfaceRepository.UserRepository
import java.io.File
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(
    private val apiService: UserService,
    private val appAuth: AppAuth
) : UserRepository {

    override suspend fun signIn(login: String, pass: String) {
        try {
            val response = apiService.singIn(login, pass)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            appAuth.setAuth(body.id, body.token)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun signUp(login: String, pass: String, name: String, media: File  ) {
        try {
            val part = if (media.exists()) {
                MultipartBody.Part.createFormData("file", media.name, media.asRequestBody())
            } else null

            val loginReg = login.toRequestBody("text/plain".toMediaTypeOrNull())
            val passReg = pass.toRequestBody("text/plain".toMediaTypeOrNull())
            val nameReg = name.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = apiService.singUp(loginReg, passReg, nameReg, part)

            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            appAuth.setAuth(body.id, body.token)

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun getUsers() {
        TODO("Not yet implemented")
    }

    override suspend fun getUser() {
        TODO("Not yet implemented")
    }
}