package pl.jarekwasowski.githubrclient.client;

import io.micrometer.core.annotation.Timed;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.jarekwasowski.githubrclient.exception.InsufficientConfigurationException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Log4j2
class RestGitHubClient implements GitHubClient {

    private final WebClient webClient;

    @Autowired
    public RestGitHubClient(WebClient.Builder builder, RestGitHubClientData clientData) {
        log.debug("Creating RestGitHubClient.");

        if (clientData.getPassword() == null || clientData.getUsername() == null ||
                clientData.getPassword().isEmpty() || clientData.getUsername().isEmpty()) {
            throw new InsufficientConfigurationException("FailFirst: You MUST set Github username and password.");
        }

        webClient = builder
                .baseUrl("https://api.github.com/")
                .defaultHeaders(httpHeaders ->
                        httpHeaders.setBasicAuth(clientData.getUsername(), clientData.getPassword()))
                .build();
    }

    protected RestGitHubClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    @Timed
    public Flux<Repository> getUserRepositories(String userName) {
        log.debug("getUserRepositories for {}", userName);

        return webClient
                .get()
                .uri(String.format("/users/%s/repos", userName))
                .retrieve()
                .bodyToFlux(Repository.class)
                .onErrorResume(
                        WebClientResponseException.NotFound.class, e -> Mono.error(new UserNotFoundException()));

    }

    @Override
    @Timed
    public Flux<Branch> getBranchesForRepository(String userName, String repositoryName) {
        log.debug("getBranchesForRepository for user {} repo {}", userName, repositoryName);

        return webClient
                .get()
                .uri(String.format("repos/%s/%s/branches", userName, repositoryName))
                .retrieve()
                .bodyToFlux(Branch.class);
    }
}
