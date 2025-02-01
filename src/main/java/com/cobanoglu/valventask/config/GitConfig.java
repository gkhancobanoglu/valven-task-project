package com.cobanoglu.valventask.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitConfig {

    // GitHub Konfigürasyonu
    @Value("${github.owner}")
    private String githubOwner;

    @Value("${github.repo}")
    private String githubRepo;

    // GitLab Konfigürasyonu
    @Value("${gitlab.project-id}")
    private String gitlabProjectId;

    @Value("${gitlab.author-name}")
    private String gitlabAuthorName;

    public String getGithubOwner() {
        return githubOwner;
    }

    public String getGithubRepo() {
        return githubRepo;
    }

    public String getGitlabProjectId() {
        return gitlabProjectId;
    }

    public String getGitlabAuthorName() {
        return gitlabAuthorName;
    }
}
