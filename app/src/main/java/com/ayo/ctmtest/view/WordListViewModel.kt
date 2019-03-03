package com.ayo.ctmtest.view

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.ayo.ctmtest.TextAnalyser
import com.ayo.ctmtest.CoroutineContextProvider
import com.ayo.ctmtest.data.Word
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import kotlin.coroutines.CoroutineContext

class WordListViewModel : ViewModel(), CoroutineScope {

    private val job = Job()
    private val contextPool = CoroutineContextProvider()
    override val coroutineContext: CoroutineContext
        get() = contextPool.Main + job


    val event = MutableLiveData<Event>()//todo make single live event

    @UseExperimental(ObsoleteCoroutinesApi::class)
    @ExperimentalCoroutinesApi
    fun loadWordListFromUrl(url: String) {
        event.value = Event.WordList(true, null, null)
        launch(context = contextPool.IO) {
            TextAnalyser.apply {
                getTextFromUrl(url)?.let {
                    getWordSetAsync(it).consumeEach { words ->
                        event.postValue(Event.WordList(words?.isEmpty() != true, words, null))

                    }
                }
            }
        }
    }

    sealed class Event {
        data class WordList(val loading: Boolean?, val data: List<Word>?, val exception: Exception?) : Event()
    }

}