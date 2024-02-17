package com.simonegiusso.java21.virtualthreads

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class BonusKotlinCoroutinesTests {

    /**
     * Coroutines are lighter than virtual threads. They are implemented on top of the JVM by means of the Kotlin compiler through callbacks. No physical objects are created!
     */
    @Test
    fun run5MCoroutines() = runBlocking {
        repeat(5_000_000) {
            launch {
                blockingOperation()
            }
        }
    }

    private suspend fun blockingOperation() = delay(500) // E.g. I/O
}