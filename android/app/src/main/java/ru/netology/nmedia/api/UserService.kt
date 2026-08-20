package ru.netology.nmedia.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.dto.PushToken
import ru.netology.nmedia.dto.Users

interface UserService {
    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun singIn(
        @Part("login") login: String,
        @Part("pass") pass: String?
    ): Response<AuthState>

    @Multipart
    @POST("users/registration")
    suspend fun singUp(
        @Part("login") login: RequestBody,
        @Part("pass") pass: RequestBody?,
        @Part("name") name: RequestBody?,
        @Part media: MultipartBody.Part?
    ): Response<AuthState>

    @POST("users/push-tokens")
    suspend fun pushToken(@Body token: PushToken)

    @GET("users")
    suspend fun getUsers(): Response<List<Users>>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id : Long) : Response<Users>
}