package com.dicoding.storyapp.core.data.remote

import com.dicoding.storyapp.core.data.remote.requestbody.Login
import com.dicoding.storyapp.core.data.remote.requestbody.Register
import com.dicoding.storyapp.core.data.remote.response.DetailStoryResponse
import com.dicoding.storyapp.core.data.remote.response.GeneralResponse
import com.dicoding.storyapp.core.data.remote.response.LoginResponse
import com.dicoding.storyapp.core.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("register")
    suspend fun register(
        @Body requestBody: Register
    ): GeneralResponse

    @POST("login")
    suspend fun login(
        @Body requestBody: Login
    ): LoginResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lan: Float? = null,
        @Part("lon") long: Float? = null,
    ): GeneralResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
    ): StoriesResponse

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailStoryResponse

}