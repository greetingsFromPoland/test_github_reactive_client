package pl.jarekwasowski.githubrclient.userinfo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.jarekwasowski.githubrclient.client.Branch;
import pl.jarekwasowski.githubrclient.client.GitHubClient;
import pl.jarekwasowski.githubrclient.client.Repository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceTest {


    @Mock
    private GitHubClient gitHubClient;

    @InjectMocks
    private UserInfoService userInfoService;


    @Test
    void getUserRepositoriesShortInfo() {

        when(gitHubClient.getUserRepositories(matches("test"))).thenReturn(
                Flux.just(new Repository("test", "r1", false),
                        new Repository("test", "r2", true),
                        new Repository("test", "r3", false),
                        new Repository("test", "r4", true)));

        when(gitHubClient.getBranchesForRepository(matches("test"), startsWith("r")))
                .thenAnswer(invocationOnMock -> {

                    switch (invocationOnMock.getArgument(1, String.class)) {
                        case "r1":
                            return Flux.just(new Branch("b1a", "sha12310"),
                                    new Branch("b1b", "sha12311"),
                                    new Branch("b1c", "sha12312"));
                        case "r2":
                            return Flux.just(new Branch("b12a", "sha12321"),
                                    new Branch("b12b", "sha12322"),
                                    new Branch("b12c", "sha12323"));
                        case "r3":
                            return Flux.just(new Branch("b123a", "sha123234"),
                                    new Branch("b123b", "sha123235"),
                                    new Branch("b123c", "sha123236"));
                        case "r4":
                            return Flux.just(new Branch("b1234a", "sha1232347"),
                                    new Branch("b1234b", "sha1232348"),
                                    new Branch("b1234c", "sha1232349"));
                        default:
                            return null;
                    }

                });

        Flux<UserRepositoryShortInfo> userRepositories = userInfoService.getUserRepositoriesShortInfo("test");

        StepVerifier
                .create(userRepositories)
                .expectNextMatches(userRepositoryShortInfo ->
                        (userRepositoryShortInfo.getName().equals("r1") &&
                                userRepositoryShortInfo.getOwnerName().equals("test") &&
                                userRepositoryShortInfo.getBranches().size() == 3 &&
                                userRepositoryShortInfo.getBranches().get(0).getName().equals("b1a") &&
                                userRepositoryShortInfo.getBranches().get(0).getLastCommitSha().equals("sha12310"))
                        || (userRepositoryShortInfo.getName().equals("r3") &&
                                userRepositoryShortInfo.getOwnerName().equals("test") &&
                                userRepositoryShortInfo.getBranches().size() == 3 &&
                                userRepositoryShortInfo.getBranches().get(0).getName().equals("b123a") &&
                                userRepositoryShortInfo.getBranches().get(0).getLastCommitSha().equals("sha123234"))
                )
                .expectNextCount(1)
                .verifyComplete();
    }
}