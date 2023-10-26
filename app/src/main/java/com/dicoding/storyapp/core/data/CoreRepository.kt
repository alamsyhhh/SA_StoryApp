package com.dicoding.storyapp.core.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.storyapp.core.data.local.preference.AutoLoginPreference
import com.dicoding.storyapp.core.data.remote.ApiService
import com.dicoding.storyapp.core.data.remote.requestbody.Login
import com.dicoding.storyapp.core.data.remote.requestbody.Register
import com.dicoding.storyapp.core.data.remote.response.DetailStoryResponse
import com.dicoding.storyapp.core.data.remote.response.GeneralResponse
import com.dicoding.storyapp.core.data.remote.response.ListStoryItem
import com.dicoding.storyapp.core.data.remote.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CoreRepository private constructor(
    private val apiService: ApiService
){

    suspend fun registerUser(
        nama: String,
        password: String,
        email: String
    ): Result<GeneralResponse> {
        return try {
            val response = apiService.register(Register(nama, email, password))
            Result.Success(response)

        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    fun loginUser(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(Login(email, password))
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun savePreference(token: String, context: Context) {
        val settingPreference = AutoLoginPreference(context)
        settingPreference.setUser(token)
    }

    fun getPreference(context: Context): String? {
        val settingPreference = AutoLoginPreference(context)
        return settingPreference.getUser()
    }

    fun getAllStories(token: String): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllStories("Bearer $token").listStory
            emit(Result.Success(response))

        }catch (e: Exception){
            emit(Result.Error(e.message.toString()))

        }
    }

    fun getStoryDetail(token: String, id: String): LiveData<Result<DetailStoryResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getStoryDetail(token, id)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    suspend fun addNewStory(
        token: String,
        image: MultipartBody.Part,
        desc: RequestBody
    ): Result<GeneralResponse> {
        return try {
            val response = apiService.addNewStory(token, image, desc)
            Result.Success(response)
        }  catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    companion object {
        @Volatile
        private var instance: CoreRepository? = null
        fun getInstance(
            apiService: ApiService
        ): CoreRepository =
            instance ?: synchronized(this) {
                instance ?: CoreRepository(apiService)
            }.also { instance = it }
    }
}