package pl.jarekwasowski.githubrclient.client;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class RestGitHubClientData {

    @Value("${github.username}")
    private String username;

    @Value("${github.password}")
    private String password;
}
