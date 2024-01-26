package com.simonegiusso.java21.virtualthreads;

import com.simonegiusso.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.PostgreSQLR2DBCDatabaseContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

import static com.simonegiusso.TestHelper.*;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

@Slf4j
@Testcontainers
public class VirtualThreadTest {

    @Container
    public PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.1"));

    @Test
    public void testSynchronousProgramming() throws SQLException {
        performSlowQuery(postgresql);
        aQuickTask();
    }

    @Test
    public void testAsynchronousProgramming() {
        try (ExecutorService singleThreadExecutor = newSingleThreadExecutor()) {
            singleThreadExecutor.submit(() -> performSlowQuery(postgresql));
            singleThreadExecutor.submit(() -> performSlowQuery(postgresql));
            aQuickTask();
        }
    }

    @Test
    public void testReactiveProgramming() {
        performReactiveSlowQuery(PostgreSQLR2DBCDatabaseContainer.getOptions(postgresql))
            .zipWith(performReactiveSlowQuery(PostgreSQLR2DBCDatabaseContainer.getOptions(postgresql)))
            .zipWith(Mono.fromCallable(TestHelper::aQuickTask))
            .subscribe();

        sleepForMs(15000); // Give Time To complete queries
    }


}