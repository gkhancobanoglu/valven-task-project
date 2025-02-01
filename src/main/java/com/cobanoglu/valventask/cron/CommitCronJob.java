package com.cobanoglu.valventask.cron;

import com.cobanoglu.valventask.service.GitLabService;
import com.cobanoglu.valventask.service.GitService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CommitCronJob {

    private final GitService gitService;
    private final GitLabService gitLabService;

    public CommitCronJob(GitService gitService, GitLabService gitLabService) {
        this.gitService = gitService;
        this.gitLabService = gitLabService;
    }

    // Saatte bir GitHub commitlerini çekme işlemi
    @Scheduled(cron = "0 0 * * * ?") // Her saat başında çalışır
    public void fetchGitHubCommitsHourly() {
        System.out.println("Fetching commits from GitHub...");
        gitService.fetchAndSaveGitHubCommitsWithPatch();
        System.out.println("GitHub commit fetching completed.");
    }

    // Saatte bir GitLab commitlerini çekme işlemi
    @Scheduled(cron = "0 0 * * * ?") // Her saat başında çalışır
    public void fetchGitLabCommitsHourly() {
        System.out.println("Fetching commits from GitLab...");
        gitLabService.fetchAndSaveGitLabCommits();
        System.out.println("GitLab commit fetching completed.");
    }

    // Eğer ayda bir çalıştırmak isterseniz, cron expression'ı değiştirin:
    // @Scheduled(cron = "0 0 0 1 * ?") // Her ayın ilk günü saat 00:00'da çalışır
}
