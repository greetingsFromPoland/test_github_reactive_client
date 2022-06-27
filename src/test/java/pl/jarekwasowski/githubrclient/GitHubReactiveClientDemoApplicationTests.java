package pl.jarekwasowski.githubrclient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnabledIfSystemProperty(named = "spring.profiles.active", matches = "dev")
class GitHubReactiveClientDemoApplicationTests {

    @Test
    void contextLoads() {
    }

}
