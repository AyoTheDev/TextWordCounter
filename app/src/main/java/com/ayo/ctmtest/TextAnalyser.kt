package com.ayo.ctmtest

import android.util.Log
import com.ayo.ctmtest.data.Word
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import java.net.URLDecoder
import java.util.regex.Pattern

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
            send(list.sortedWith(compareByDescending(Word::count)))
        }
        send(emptyList<Word>())
    }

    private fun addOrUpdateWordToList(list: MutableList<Word>, word: String) {
        if (!list.any { it.key == word })
            list.add(Word(key = word, count = 1, isPrime = false))
        else {
            list.first { it.key == word }
                    .apply {
                        ++count
                        isPrime = count.isPrimeCompute()
                    }
        }
    }

    @ExperimentalCoroutinesApi
    private suspend fun addOrUpdateWordToListWithRegex(list: MutableList<Word>, data: String, producer: ProducerScope<List<Word>>) {
        val dataLowerCase = data.toLowerCase()
        Regex("\\(").replace(dataLowerCase, "")
        Regex("\\)").replace(dataLowerCase, "")
        parseListFromRawData(data).forEach {
            var i = 0
            val p = Pattern.compile(it)
            val m = p.matcher(dataLowerCase)
            while (m.find()) {
                i++
            }
            list.add(Word(key = it, count = i, isPrime = i.isPrimeComputeStatically()))
            producer.send(list.sortedWith(compareByDescending(Word::count)))
        }

    }

    private fun parseListFromRawData(data: String): List<String> {
        val string = URLDecoder.decode(data, "UTF-8")
        Regex("[^A-Za-z' ]").replace(string, "")
        return Regex("\\s+").split(string).map { it.toLowerCase() }
    }


    fun getTextFromUrl(bookUrl: String): String? = try {
        val stringBuilder = StringBuilder()
        val url = URL(bookUrl)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 60000
        val input = BufferedReader(InputStreamReader(conn.inputStream))
        input.forEachLine { stringBuilder.append(it) }
        input.close()
        stringBuilder.toString()
    } catch (e: Exception) {
        Log.d("Error: ", e.toString())
        null
    }
}