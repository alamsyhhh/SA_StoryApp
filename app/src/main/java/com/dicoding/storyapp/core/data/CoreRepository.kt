package com.dicoding.storyapp.core.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.core.data.local.paging.StoryDatabase
import com.dicoding.storyapp.core.data.local.paging.StoryEntity
import com.dicoding.storyapp.core.data.local.paging.StoryRemoteMediator
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
    private val apiService: ApiService,
    private val storyDb: StoryDatabase
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

    fun getStoriesWithLocation(token: String): LiveData<Result<List<ListStoryItem>>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getAllStories(token, 1,100, 1)
                emit(Result.Success(response.listStory))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getAllStories(token: String): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDb, apiService, token),
            pagingSourceFactory = {
                storyDb.storyDao().getAllStory()
            }
        ).liveData
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
        desc: RequestBody,
        lat: Float?=null,
        lan: Float?=null
    ): Result<GeneralResponse> {
        return try {
            val response = apiService.addNewStory(token = token, file = image, description = desc, lon = lan ,lat = lat)
            Result.Success(response)
        }  catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    companion object {
        @Volatile
        private var instance: CoreRepository? = null
        fun getInstance(
            apiService: ApiService,
            storyDb: StoryDatabase
        ): CoreRepository =
            instance ?: synchronized(this) {
                instance ?: CoreRepository(apiService,storyDb)
            }.also { instance = it }
    }
}