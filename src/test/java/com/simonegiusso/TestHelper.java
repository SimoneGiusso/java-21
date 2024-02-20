package com.simonegiusso;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.JdbcDatabaseContainer;
import reactor.core.publisher.Mono;
import reactor.netty.resources.LoopResources;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ThreadLocalRandom;

import static io.r2dbc.postgresql.PostgresqlConnectionFactoryProvider.LOOP_RESOURCES;
import static java.lang.String.format;
import static java.lang.Thread.sleep;

@UtilityClass
@Slf4j
public class TestHelper {

    public static int performSlowQuery(JdbcDatabaseContainer<?> container) throws SQLException {
        DataSource ds = getDataSource(container);
        ResultSet resultSet;
        try (Statement statement = ds.getConnection().createStatement()) {
            log.info("Performing slow query...");
            statement.execute(slowQuery());
            log.info("Performing slow query succeed!");
            resultSet = statement.getResultSet();
            resultSet.next();
            return resultSet.getInt(2);
        }

    }

    public static Mono<Integer> performReactiveSlowQuery(ConnectionFactoryOptions connectionFactoryOptions) {
        ConnectionFactory connectionFactory = ConnectionFactories.get(
            connectionFactoryOptions.mutate()
                .option(LOOP_RESOURCES, LoopResources.create("r2dbc-postgresql", 1, true))
                .build()
        );

        return Mono.from(Mono.from(connectionFactory.create())
                .doOnNext(x -> log.info("Performing slow query..."))
                .flatMap(connection -> Mono.from(
                    connection.createStatement(slowQuery()).execute())
                )
                .doOnNext(x -> log.info("Performing slow query succeed!")))
            .flatMap(result -> Mono.from(result.map(readable -> readable.get(1, Integer.class))));

    }

    private static DataSource getDataSource(JdbcDatabaseContainer<?> container) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(container.getJdbcUrl());
        hikariConfig.setUsername(container.getUsername());
        hikariConfig.setPassword(container.getPassword());
        hikariConfig.setDriverClassName(container.getDriverClassName());
        return new HikariDataSource(hikariConfig);
    }

    private static String slowQuery() {
        int sleepFor = ThreadLocalRandom.current().nextInt(2, 8);
        return format("SELECT pg_sleep(%s), 1 AS data;", sleepFor);
    }

    public static void sleepForMs(int ms) {
        try {
            sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}