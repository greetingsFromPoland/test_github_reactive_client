package pl.jarekwasowski.githubrclient.userinfo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserRepositoryShortInfo {

    private String ownerName;
    private String name;
    private List<BranchSummary> branches;

    @Builder
    @Data
    public static class BranchSummary {
        private String name;
        private String lastCommitSha;

    }
}
