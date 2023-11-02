package com.dicoding.storyapp.app.maps

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.core.data.CoreRepository

class MapsViewModel(private val repositoryMain: CoreRepository) : ViewModel()  {


    fun getStoriesWithLocation(token: String) = repositoryMain.getStoriesWithLocation(token)
}