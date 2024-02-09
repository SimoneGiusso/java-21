package com.simonegiusso.java21.virtualthreads;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static java.lang.Thread.currentThread;

@Slf4j
class VirtualThreadsTest {

    private final Runnable LOG_ME = () -> log.info(String.valueOf(currentThread()));

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


}