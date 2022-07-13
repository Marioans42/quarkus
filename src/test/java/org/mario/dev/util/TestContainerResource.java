package org.mario.dev.util;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TestContainerResource implements QuarkusTestResourceLifecycleManager {

    static PostgreSQLContainer<?> DATABASE =
            new PostgreSQLContainer<>("postgres:14.2")
                    .withDatabaseName("tododb")
                    .withUsername("todouser")
                    .withPassword("todopw");
    @Override
    public Map<String, String> start() {
        DATABASE.start();
        return Collections.singletonMap(
                "quarkus.datasource.url", DATABASE.getJdbcUrl()
        );
    }

    @Override
    public void stop() {
        DATABASE.stop();
    }
}
