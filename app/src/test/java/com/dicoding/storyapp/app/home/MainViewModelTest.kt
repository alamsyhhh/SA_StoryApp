package com.dicoding.storyapp.app.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.storyapp.app.home.paging.StoryPagingAdapter
import com.dicoding.storyapp.app.home.paging.StroryDiffCallback
import com.dicoding.storyapp.app.home.utils.DataDummy
import com.dicoding.storyapp.app.home.utils.MainDispatcherRule
import com.dicoding.storyapp.app.home.utils.getOrAwaitValue
import com.dicoding.storyapp.core.data.CoreRepository
import com.dicoding.storyapp.core.data.local.paging.StoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var coreRepository: CoreRepository

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()

        val data: PagingData<StoryEntity> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<StoryEntity>>()
        expectedStory.value=data

        `when`(coreRepository.getAllStories("token")).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(coreRepository)
        val actualStory: PagingData<StoryEntity> = mainViewModel.getStories("token").getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StroryDiffCallback(),
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size,differ.snapshot().size)
        assertEquals(dummyStory[0],differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`()= runTest {
        val data: PagingData<StoryEntity> = PagingData.from(emptyList())
        val expectedData = MutableLiveData<PagingData<StoryEntity>>()
        expectedData.value = data

        `when`(coreRepository.getAllStories("token")).thenReturn(expectedData)

        val mainViewModel = MainViewModel(coreRepository)
        val actualData: PagingData<StoryEntity> = mainViewModel.getStories("token").getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualData)
        assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource: PagingSource<Int, LiveData<List<StoryEntity>>>(){
    companion object {
        fun snapshot(item: List<StoryEntity>): PagingData<StoryEntity>{
            return PagingData.from(item)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryEntity>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryEntity>>> {
        return LoadResult.Page(emptyList(),0,1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}