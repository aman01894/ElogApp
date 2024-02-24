package com.example.elogapp.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Coroutines {

    fun main(work: suspend (() -> Unit)) = CoroutineScope(Dispatchers.Main).launch {
        work()
    }

    fun mainIO(work: suspend (() -> Unit)) = CoroutineScope(Dispatchers.IO).launch {
        work()
    }
    fun mainDefault(work: suspend (() -> Unit)) = CoroutineScope(Dispatchers.Default).launch {
        work()
    }

}