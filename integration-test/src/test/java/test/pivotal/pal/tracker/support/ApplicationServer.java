package test.pivotal.pal.tracker.support;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.assertj.core.api.Assertions.fail;
import static test.pivotal.pal.tracker.support.MapBuilder.envMapBuilder;

public class ApplicationServer {

    private final String jarPath;
    private final String port;

    private Process serverProcess;

    public ApplicationServer(String jarPath, String port) {
        this.jarPath = jarPath;
        this.port = port;
    }


    public void start(Map<String, String> env) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder()
            .command("java", "-jar", jarPath)
            .inheritIO();

        processBuilder.environment().put("SERVER_PORT", port);
        env.forEach((key, value) -> processBuilder.environment().put(key, value));

        serverProcess = processBuilder.start();
    }

    public void startWithDatabaseName(String dbName) throws IOException, InterruptedException {
        String dbUrl = "jdbc:mysql://localhost:3306/" + dbName + "?useSSL=false&useTimezone=true&serverTimezone=UTC&useLegacyDatetimeCode=false";

        start(envMapBuilder()
            .put("SPRING_DATASOURCE_URL", dbUrl)
            .put("EUREKA_CLIENT_ENABLED", "false")
            .put("RIBBON_EUREKA_ENABLED", "false")
            .put("REGISTRATION_SERVER_RIBBON_LISTOFSERVERS", "http://localhost:8883")
            .put("APPLICATION_OAUTH_ENABLED", "false")
            .build()
        );
    }

    public void stop() {
        serverProcess.destroyForcibly();
    }

    public static void waitOnPorts(String... ports) throws InterruptedException {
        for (String port : ports) waitUntilServerIsUp(port);
    }

    private static void waitUntilServerIsUp(String port) throws InterruptedException {
        HttpClient httpClient = new HttpClient();
        int timeout = 120;
        Instant start = Instant.now();
        boolean isUp = false;

        System.out.print("Waiting on port " + port + "...");

        while (!isUp) {
            try {
                httpClient.get("http://localhost:" + port);
                isUp = true;
                System.out.println(" server is up.");
            } catch (Throwable e) {

                long timeSpent = ChronoUnit.SECONDS.between(start, Instant.now());
                if (timeSpent > timeout) {
                    fail("Timed out waiting for server on port " + port);
                }

                System.out.print(".");
                Thread.sleep(200);
            }
        }
    }
}

