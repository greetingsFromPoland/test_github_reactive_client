package pl.jarekwasowski.githubrclient.client;

import reactor.core.publisher.Flux;

public interface GitHubClient {

    Flux<Repository> getUserRepositories(String userName);

    Flux<Branch> getBranchesForRepository(String userName, String repositoryName);
}
