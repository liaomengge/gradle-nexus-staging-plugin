package io.codearte.gradle.nexus;

import io.codearte.gradle.nexus.infra.SimplifiedHttpJsonRestClient
import io.codearte.gradle.nexus.logic.RepositoryFetcher
import io.codearte.gradle.nexus.logic.RepositoryCloser;
import io.codearte.gradle.nexus.logic.StagingProfileFetcher;
import org.gradle.api.tasks.TaskAction;

public class CloseRepositoryTask extends BaseStagingTask {

    @TaskAction
    void doAction() {
        StagingProfileFetcher stagingProfileFetcher = createFetcherWithGivenClient(createClient())
        RepositoryFetcher openRepositoryFetcher = createRepositoryFetcherWithGivenClient(createClient())
        RepositoryCloser repositoryCloser = createRepositoryCloserWithGivenClient(createClient())

        String stagingProfileId = stagingProfileFetcher.getStagingProfileIdForPackageGroup(getPackageGroup())
        String repositoryId = openRepositoryFetcher.getOpenRepositoryIdForStagingProfileId(stagingProfileId)
        repositoryCloser.closeRepositoryWithIdAndStagingProfileId(repositoryId, stagingProfileId)
    }

    private StagingProfileFetcher createFetcherWithGivenClient(SimplifiedHttpJsonRestClient client) {
        return new StagingProfileFetcher(client, getNexusUrl())
    }

    private RepositoryFetcher createRepositoryFetcherWithGivenClient(SimplifiedHttpJsonRestClient client) {
        return new RepositoryFetcher(client, getNexusUrl())
    }

    private RepositoryCloser createRepositoryCloserWithGivenClient(SimplifiedHttpJsonRestClient client) {
        return new RepositoryCloser(client, getNexusUrl())
    }
}