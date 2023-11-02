package com.dicoding.storyapp.app.addstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.storyapp.app.details.DetailsViewModel
import com.dicoding.storyapp.app.home.MainViewModel
import com.dicoding.storyapp.app.home.utils.MainDispatcherRule
import com.dicoding.storyapp.app.home.utils.getOrAwaitValue
import com.dicoding.storyapp.core.data.CoreRepository
import com.dicoding.storyapp.core.data.Result
import com.dicoding.storyapp.core.data.remote.response.GeneralResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var coreRepository: CoreRepository


    //Test Tambah Story
    @ExperimentalCoroutinesApi
    @Test
    fun `When AddNewStory Success Should Not Null and Return Success`() = runTest {
        //Dummy data
        val token = "token"
        val imageFile = File("test_image.png")
        val imageRequestBody = imageFile.asRequestBody("image/png".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)
        val desc = "Sample description".toRequestBody("text/plain".toMediaTypeOrNull())


        val expectedResponseValue = GeneralResponse(false, "Story added successfully")

        val expectedResponse = MutableLiveData<Result<GeneralResponse>>()
        expectedResponse.value = Result.Success(expectedResponseValue)


        `when`(
            coreRepository.addNewStory(
                token,
                imagePart,
                desc
            )
        ).thenReturn(expectedResponse.value)

        val addStoryViewModel = AddStoryViewModel(coreRepository)
        val actualResponse = addStoryViewModel.addNewStory(token, imagePart, desc).getOrAwaitValue()

        Mockito.verify(coreRepository).addNewStory(token, imagePart, desc)

        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertEquals(expectedResponseValue, (actualResponse as Result.Success).data)

    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When AddNewStory Network Error Should Return Error`()= runTest{

        //Dummy data
        val token = "token"
        val imageFile = File("test_image.png")
        val imageRequestBody = imageFile.asRequestBody("image/png".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)
        val desc = "Sample description".toRequestBody("text/plain".toMediaTypeOrNull())

        val expectedResponse = MutableLiveData<Result<GeneralResponse>>()
        expectedResponse.value = Result.Error("Error") //set expected respond value

        `when`(
            coreRepository.addNewStory(
                token,
                imagePart,
                desc
            )
        ).thenReturn(expectedResponse.value)

        val addStoryViewModel = AddStoryViewModel(coreRepository)
        val actualResponse = addStoryViewModel.addNewStory(token, imagePart, desc).getOrAwaitValue()
        Mockito.verify(coreRepository).addNewStory(token, imagePart, desc) //verify that "addNewStory()" in repositoryMain is invoked during test

        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }

}