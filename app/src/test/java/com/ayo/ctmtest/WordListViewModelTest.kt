package com.ayo.ctmtest

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import com.ayo.ctmtest.utils.TestContextProvider
import com.ayo.ctmtest.view.WordListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class WordListViewModelTest {

    private lateinit var underTest: WordListViewModel
    @Mock
    private lateinit var eventObserver: Observer<WordListViewModel.Event>
    @Mock
    lateinit var lifecycleOwner: LifecycleOwner

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        lifecycle = LifecycleRegistry(lifecycleOwner)
        underTest = WordListViewModel()
        underTest.inject(TestContextProvider())
        Mockito.`when`(lifecycleOwner.lifecycle).thenReturn(lifecycle)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        underTest.event.observeForever(eventObserver)
    }

    @After
    fun finish() {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun word_list_data() = runBlocking {

        //GIVEN
        val url = "https://www.w3.org/TR/PNG/iso_8859-1.txt"

        //WHEN
        underTest.loadWordListFromUrl(url)

        //THEN
        Assert.assertTrue(
                underTest.event.value ==
                        WordListViewModel.Event.WordList(Mockito.anyBoolean(), Mockito.anyList(), null)
        )

    }
}