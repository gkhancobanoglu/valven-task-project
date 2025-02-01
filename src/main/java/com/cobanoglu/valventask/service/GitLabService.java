package com.cobanoglu.valventask.service;

import com.cobanoglu.valventask.config.GitConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cobanoglu.valventask.model.Commit;
import com.cobanoglu.valventask.model.Developer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class GitLabService {

    private final RestTemplate restTemplate;
    private final CommitService commitService;
    private final DeveloperService developerService;

    private final String gitlabProjectId;
    private final String authorName;

    public GitLabService(CommitService commitService, DeveloperService developerService, GitConfig gitConfig) {
        this.restTemplate = new RestTemplate();
        this.commitService = commitService;
        this.developerService = developerService;
        this.gitlabProjectId = gitConfig.getGitlabProjectId();
        this.authorName = gitConfig.getGitlabAuthorName();
    }

    // Uygulama başlangıcında GitLab commitlerini çek ve kaydet
    @PostConstruct
    public void fetchAndSaveGitLabCommitsOnStartup() {
        System.out.println("Fetching commits from GitLab...");
        fetchAndSaveGitLabCommits();
        System.out.println("GitLab commit fetching completed.");
    }

    // GitLab'dan commitleri al ve kaydet
    public void fetchAndSaveGitLabCommits() {
        String url = buildGitLabApiUrl(gitlabProjectId);

        try {
            // GitLab API'den JSON yanıtını al
            String jsonResponse = restTemplate.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // Mevcut geliştiriciyi al
            Developer developer = developerService.getDeveloper();

            for (JsonNode commitNode : rootNode) {
                // Sadece `author_name` "George Nachman" olan commitleri işliyoruz.
                if (commitNode.get("author_name").asText().equalsIgnoreCase(authorName)) {
                    Commit commit = new Commit();

                    // Hash'e "-gitlab" ekle
                    String hashWithGitlab = commitNode.get("id").asText() + "-gitlab";
                    commit.setHash(hashWithGitlab);

                    commit.setPlatform("GitLab");

                    // Commit detaylarını al
                    commit.setMessage(commitNode.get("message").asText());
                    commit.setAuthorName(commitNode.get("author_name").asText());
                    commit.setAuthorEmail(commitNode.get("author_email").asText());
                    commit.setTimestamp(LocalDateTime.parse(commitNode.get("authored_date").asText(), DateTimeFormatter.ISO_DATE_TIME));

                    // Patch bilgisi al
                    String patch = fetchPatchFromGitLab(gitlabProjectId, commitNode.get("id").asText());
                    if (patch != null && patch.length() > 5000) {
                        patch = patch.substring(0, 500);
                    }
                    commit.setPatch(patch);

                    // Developer ekle
                    commit.setDeveloper(developer);

                    // Commit'i kaydet
                    commitService.saveCommit(commit);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GitLab API'den patch bilgisini çeken yöntem
    private String fetchPatchFromGitLab(String projectId, String commitId) {
        String url = String.format("https://gitlab.com/api/v4/projects/%s/repository/commits/%s/diff", projectId, commitId);
        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            StringBuilder patchBuilder = new StringBuilder();
            for (JsonNode diffNode : rootNode) {
                String diff = diffNode.get("diff").asText();
                patchBuilder.append(diff).append("\n");
            }
            return patchBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String buildGitLabApiUrl(String projectId) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        String since = oneMonthAgo.format(DateTimeFormatter.ISO_DATE_TIME);
        return String.format("https://gitlab.com/api/v4/projects/%s/repository/commits?since=%s&per_page=100", projectId, since);
    }
}
