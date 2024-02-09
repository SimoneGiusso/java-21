package com.simonegiusso.java21.virtualthreads;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.stream.IntStream;

import static java.lang.Math.round;
import static java.lang.Runtime.getRuntime;
import static java.lang.Thread.sleep;
import static java.util.concurrent.Executors.*;

@Slf4j
class VirtualThreadPerformanceTest {

    private final static int N_PLATFORM_THREADS = 4_000;
    private final static int N_VIRTUAL_THREADS = 5_000_000;

    @Test
    void platformThreads() {
        try (var executor = newCachedThreadPool()) {
            IntStream.range(0, N_PLATFORM_THREADS)
                .forEach(i -> executor.submit(VirtualThreadPerformanceTest::blockingOperation));
        }
    }

    /**
     * Virtual threads are not faster threads — they do not run code any faster than platform threads. They exist to provide higher throughput
     */
    @Test
    void virtualThreads() {
        log.info("{} blocking tasks for 0.5s for a total duration of {} days", N_VIRTUAL_THREADS, Duration.ofSeconds(round(N_VIRTUAL_THREADS * 0.5))
            .toDays());
        try (var executor = newVirtualThreadPerTaskExecutor()) { // Another way to create virtual thread
            IntStream.range(0, N_VIRTUAL_THREADS)
                .forEach(i -> executor.submit(VirtualThreadPerformanceTest::blockingOperation));
        }
    }

    @Test
    void platformThreadsThroughput() {
        log.info("Running 50'000 tasks of 0.5s each with 4000 threads should finish in a bit more than {}s: ", 50_000 / 4_000 * 0.5);
        try (var executor = newFixedThreadPool(4000)) {
            IntStream.range(0, 50_000).forEach(i -> executor.submit(() -> {
                if (i % 2000 == 0) {
                    log.info("Doing some work...");
                }
                blockingOperation();
                if (i % 2000 == 0) {
                    log.info("I finished!");
                }
            }));
        }  // executor.close() is called implicitly, and waits
    }

    /**
     * Virtual threads are suitable for executing tasks that spend most of
     * the time blocked, often waiting for I/O operations to complete. Virtual threads
     * are not intended for long running CPU intensive operations.
     */
    @Test
    void virtualThreadsThroughput() {
        log.info("Running 50'000 tasks of 0.5s each with virtual threads");
        try (var executor = newVirtualThreadPerTaskExecutor()) { // A pool doesn't make sense with VT
            IntStream.range(0, 50_000).forEach(i -> executor.submit(() -> {
                if (i % 2000 == 0) {
                    log.info(Thread.currentThread().toString());
                }
                blockingOperation(); // Moving VT's stack to heap and back to a worker thread. Platform thread is not blocked!
            }));
        }  // executor.close() is called implicitly, and waits
    }

    @Test
    void platformThreadsCappedByProcessor() {
        log.info("Running 4'000 tasks of 0.5s each with 4000 threads with {} processors", getRuntime().availableProcessors());
        try (var executor = newFixedThreadPool(4000)) {
            IntStream.range(0, 4_000).forEach(i -> executor.submit(VirtualThreadPerformanceTest::blockingOperation));
        }  // executor.close() is called implicitly, and waits
    }


    private static void blockingOperation() {
        try {
            sleep(Duration.ofMillis(500));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    } // E.g. I/O

}