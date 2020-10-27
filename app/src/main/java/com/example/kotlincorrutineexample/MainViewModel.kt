package com.example.kotlincorrutineexample

import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlincorrutineexample.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.*

class MainViewModel(
    private val binding: ActivityMainBinding
): ViewModel() {

    private val maxCnt = 2_500_000

    fun sequentialFunction()
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            sequentialExec()
        }
    }

    fun concurrentFunction()
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            concurrentExec()
        }
    }

    private suspend fun sequentialExec()
    {
        //Even though the operation itself is sequential, the whole must be launched on a separate
        //context from the Main thread to prevent ANR
        withContext(Dispatchers.Default)
        {
            val time = measureTimeMillis {
                val one = meaninglessCounter(binding.progressBarSeq1)
                val two = meaninglessCounter(binding.progressBarSeq2)
                //val aux = one + two
            }

            val ans = "$time ms"

            changeResult(binding.resultSeqTime, ans)
        }
    }

    private suspend fun concurrentExec()
    {
        withContext(Dispatchers.Default)
        {
            val time = measureTimeMillis {
                val one = async{ meaninglessCounter(binding.progressBarCon1) }
                val two = async{ meaninglessCounter(binding.progressBarCon2) }
                runBlocking {
                    val ans = one.await() + two.await() //Forces the thread to wait until completion
                }
            }

            val ans = "$time ms"

            changeResult(binding.resultConTime, ans)
        }
    }

    private fun meaninglessCounter(pProgressBar: ProgressBar): Int
    {
        pProgressBar.progress = 0
        var cnt = pProgressBar.progress

        for(i in 1..maxCnt)
        {
            pProgressBar.progress = cnt
            cnt++
        }

        return cnt
    }

    private fun changeResult(textView: TextView, pText: String)
    {
        //Updating the text MUST be done on the Main thread, since it was in that context
        //where the textView was created
        CoroutineScope(Dispatchers.Main).launch {
            textView.text = pText
        }
    }

}