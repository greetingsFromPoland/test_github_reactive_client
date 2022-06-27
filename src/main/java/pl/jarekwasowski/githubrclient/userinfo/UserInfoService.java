package pl.jarekwasowski.githubrclient.userinfo;

import io.micrometer.core.annotation.Timed;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.jarekwasowski.githubrclient.client.GitHubClient;
import pl.jarekwasowski.githubrclient.client.Repository;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
@Log4j2
public class UserInfoService {

    private final GitHubClient gitHubClient;

    public UserInfoService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    @Timed
    Flux<UserRepositoryShortInfo> getUserRepositoriesShortInfo(String userName) {
        log.debug("getUserRepositoriesShortInfo for username {}", userName);

        return gitHubClient
                .getUserRepositories(userName)
                .parallel()
                .runOn(Schedulers.parallel())
                .filter(r -> !r.isFork())
                .map(this::map)
                .flatMap(userRepositoryShortInfo ->
                        gitHubClient.getBranchesForRepository(userRepositoryShortInfo.getOwnerName(), userRepositoryShortInfo.getName())
                                .map(b -> UserRepositoryShortInfo.BranchSummary
                                        .builder()
                                        .lastCommitSha(b.getLastCommitSha())
                                        .name(b.getName())
                                        .build())
                                .collectList()
                                .map(l -> {
                                    userRepositoryShortInfo.setBranches(l);
                                    return userRepositoryShortInfo;
                                })
                )
                .sequential();

    }

    private UserRepositoryShortInfo map(Repository repository) {
        return UserRepositoryShortInfo
                .builder()
                .ownerName(repository.getOwnerName())
                .name(repository.getName())
                .build();
    }
}
