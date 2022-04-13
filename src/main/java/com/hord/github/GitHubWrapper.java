package com.hord.github;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;

public class GitHubWrapper {

    private final GitHub gitHub;
    private final String owner;

    public GitHubWrapper(GitHub gitHub, String owner) {
        this.gitHub = gitHub;
        this.owner = owner;
    }

    public GitHub getGitHub() {
        return gitHub;
    }

    public GHRepository getRepository(String repo) throws IOException {
        return gitHub.getRepository(owner + "/" + repo);
    }
}
