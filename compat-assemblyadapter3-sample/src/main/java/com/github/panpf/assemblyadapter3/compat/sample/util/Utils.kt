package com.github.panpf.assemblyadapter3.compat.sample.util

import kotlinx.coroutines.delay

suspend fun <R> minExecuteTime(minTime: Long, block: () -> R): R {
    val startTime = System.currentTimeMillis()
    val result = block()
    val endTime = System.currentTimeMillis()
    val durationTime = endTime - startTime
    if (durationTime < minTime) {
        delay(minTime - durationTime)
    }
    return result
}