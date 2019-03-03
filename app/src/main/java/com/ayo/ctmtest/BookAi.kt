package com.ayo.ctmtest

import android.util.Log
import com.ayo.ctmtest.data.Word
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

object BookAi {

    fun getWordSet(data: String): Set<Word> {
        val list = mutableSetOf<Word>()
        Regex("[^A-Za-z' ]").replace(data, "")
        Regex("\\d").replace(data, "")
        val wordList = Regex("\\s+").split(data)

        wordList.forEach { word ->
            if (!list.any { it.key == word.toLowerCase() } && !word.contains("\"", true))
                list.add(Word(key = word.toLowerCase(), count = 1, isPrime = false))
            else
                list.first { it.key == word.toLowerCase() }.count++

        }
        list.map { Word(key = it.key, count = it.count, isPrime = it.count.isPrime()) }.toSet()

        return list
    }

    @ExperimentalCoroutinesApi
    fun CoroutineScope.getWordSetAsync(data: String): ReceiveChannel<List<Word>?> = produce {
        val list = mutableListOf<Word>()
        Regex("[^A-Za-z' ]").replace(data, "")
        Regex("\\d").replace(data, "")
        val wordList = Regex("\\s+").split(data)
        wordList.forEach { w ->
            var word = w.toLowerCase()
            if (!list.any { it.key == word })
                list.add(Word(key = word, count = 1, isPrime = false))
            else {
                list.first { it.key == word }
                        .apply {
                            ++count
                            isPrime = count.isPrime()
                        }
            }
            send(list.sortedWith(compareBy(Word::key)))
        }
        send(emptyList()) //bit cheeky
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