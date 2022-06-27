package pl.jarekwasowski.githubrclient.client;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestGitHubClientTest {

    private MockWebServer server;

    private RestGitHubClient gitHubClient;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();

        gitHubClient = new RestGitHubClient(
                WebClient
                        .builder()
                        .baseUrl(server.url("").toString())
                        .build()
        );

    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    void getUserRepositories() throws InterruptedException, URISyntaxException, IOException {
        //g
        server.enqueue(prepareValidRepositoriesMockResponse());

        //w
        Flux<Repository> repository = gitHubClient.getUserRepositories("defunkt");

        //t
        StepVerifier.create(repository)
                .expectNextMatches(repository1 ->
                        repository1.getName().equals("ace")
                                && repository1.getOwnerName().equals("defunkt") && repository1.isFork())
                .expectNextMatches(repository1 ->
                        repository1.getName().equals("acts_as_textiled")
                                && repository1.getOwnerName().equals("defunkt") && !repository1.isFork())
                .verifyComplete();

        RecordedRequest recordedRequest = server.takeRequest();

        assertThat(recordedRequest.getPath()).isEqualTo("/users/defunkt/repos");
        assertThat(recordedRequest.getMethod()).isEqualTo(HttpMethod.GET.toString());
    }

    @Test
    void getUserRepositories_user_not_found() throws InterruptedException, URISyntaxException, IOException {
        //g
        MockResponse mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setStatus("HTTP/1.1 404");
        server.enqueue(mockResponse);

        //w
        Flux<Repository> repository = gitHubClient.getUserRepositories("defunkt");

        //t
        StepVerifier.create(repository)
                .expectError(UserNotFoundException.class)
                .verify();

    }


    @Test
    void getBranchesForRepository() throws IOException, URISyntaxException {

        Path responseFile = Paths.get(
                getClass()
                        .getResource("/github_user_repo_branches.json")
                        .toURI());

        String responseBody = Files.readString(responseFile, StandardCharsets.UTF_8);

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(responseBody);

        server.enqueue(response);


        Flux<Branch> branches = gitHubClient.getBranchesForRepository("defunkt", "ace");

        StepVerifier
                .create(branches)
                .expectNextMatches(branch ->
                        branch.getName().equals("dont-hardcode-fonts-protocol") && branch.getLastCommitSha().equals("f1b0c0859bb10f557caa18cdd02afda482fc558a"))
                .expectNextCount(2)
                .verifyComplete();


    }

    private MockResponse prepareValidRepositoriesMockResponse() throws IOException, URISyntaxException {
        Path responseFile = Paths.get(
                getClass()
                        .getResource("/github_user_repository_response.json")
                        .toURI());

        String responseBody = Files.readString(responseFile, StandardCharsets.UTF_8);

        return new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(responseBody);
    }

}