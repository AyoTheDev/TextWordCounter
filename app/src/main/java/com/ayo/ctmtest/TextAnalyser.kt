package com.ayo.ctmtest

import android.util.Log
import com.ayo.ctmtest.data.Word
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

object TextAnalyser {

    fun getWordSet(data: String): List<Word> {
        val list = mutableListOf<Word>()
        parseListFromRawData(data).forEach {
            addOrUpdateWordToList(list, it)
        }
        return list
    }

    @ExperimentalCoroutinesApi
    fun CoroutineScope.getWordSetAsync(data: String): ReceiveChannel<List<Word>?> = produce {
        val list = mutableListOf<Word>()
        parseListFromRawData(data).forEach {
            addOrUpdateWordToList(list, it)
            send(list.sortedWith(compareBy(Word::key)))
        }
        send(emptyList()) //bit cheeky
    }

    private fun addOrUpdateWordToList(list: MutableList<Word>, word: String){
        if (!list.any { it.key == word })
            list.add(Word(key = word, count = 1, isPrime = false))
        else {
            list.first { it.key == word }
                    .apply {
                        ++count
                        isPrime = count.isPrime()
                    }
        }
    }
    private fun parseListFromRawData(data: String): List<String> {
        Regex("[^A-Za-z' ]").replace(data, "")
        return Regex("\\s+").split(data).map { it.toLowerCase() }
    }



    fun getTextFromUrl(bookUrl: String): String? = try {
        val stringBuilder = StringBuilder()
        val url = URL(bookUrl)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 60000 // timing out in a minute
        val input = BufferedReader(InputStreamReader(conn.inputStream))
        input.forEachLine {
            stringBuilder.append(it)
        }
        input.close()
        stringBuilder.toString()
    } catch (e: Exception) {
        Log.d("Error: ", e.toString())
        null
    }
}