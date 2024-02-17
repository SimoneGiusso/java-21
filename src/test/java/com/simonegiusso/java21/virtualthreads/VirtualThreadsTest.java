package com.simonegiusso.java21.virtualthreads;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

import static com.simonegiusso.java21.virtualthreads.VirtualThreadPerformanceTest.blockingOperation;
import static java.lang.Thread.currentThread;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;

@Slf4j
class VirtualThreadsTest {

    private static final Runnable LOG_ME = () -> log.info(String.valueOf(currentThread()));
    private static final int N_THREADS = 4_000;

    @Test
    void oldWay() throws InterruptedException {
        Thread pthread = new Thread(LOG_ME);
        pthread.start();
        pthread.join(); // Wait this thread to terminate
    }

    @Test
    void newWay() throws InterruptedException {
        Thread.ofPlatform()
            .name("platform-", 0)
            .start(LOG_ME)
            .join();

        Thread.ofVirtual()
            .name("virtual-", 0)
            .start(LOG_ME) // VT are run on top of platform thread (worker)
            .join();
    }

    /**
     * Virtual threads are very light thread. Use them for blocking operations not non-blocking ones!
     */
    @Test
    void create1MVirtualThreads() {
        IntStream.range(0, 1_000_000)
            .mapToObj(x -> Thread.ofVirtual().unstarted(LOG_ME))
            .toList();
    }

    @Test
    void create1MThreads() {
        IntStream.range(0, 1_000_000)
            .mapToObj(x -> Thread.ofPlatform().unstarted(LOG_ME))
            .toList();
    }

    @Test
    void platformThreadsResourceConsumption() {
        runWithExecutor(newCachedThreadPool());
    }

    @Test
    void virtualThreadsResourceConsumption() {
        runWithExecutor(newVirtualThreadPerTaskExecutor());
    }

    private void runWithExecutor(ExecutorService executorService) {
        log.info("Java process running with PID {}", ProcessHandle.current().pid());
        blockingOperation(10_000); // Give time to activate profile
        IntStream.range(0, N_THREADS)
            .forEach(i -> executorService.submit(VirtualThreadPerformanceTest::blockingOperationFor5s));
    }


}