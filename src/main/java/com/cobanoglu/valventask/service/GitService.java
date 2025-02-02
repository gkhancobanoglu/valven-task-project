package com.cobanoglu.valventask.service;

import com.cobanoglu.valventask.config.GitConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cobanoglu.valventask.model.Commit;
import com.cobanoglu.valventask.model.Developer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class GitService {

    private final RestTemplate restTemplate;
    private final CommitService commitService;
    private final DeveloperService developerService;
    private final String githubOwner;
    private final String githubRepo;


    public GitService(CommitService commitService, DeveloperService developerService, GitConfig gitConfig) {
        this.restTemplate = new RestTemplate();
        this.commitService = commitService;
        this.developerService = developerService;
        this.githubOwner = gitConfig.getGithubOwner(); // GitConfig'den alınan owner
        this.githubRepo = gitConfig.getGithubRepo();
    }



    // GitHub commitlerini patch bilgisiyle birlikte çek ve kaydet
    public void fetchAndSaveGitHubCommitsWithPatch() {
        String url = buildGitHubApiUrl(githubOwner, githubRepo);

        try {
            // API çağrısı ile JSON yanıtını al
            String jsonResponse = restTemplate.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // Veritabanındaki mevcut geliştiriciyi al
            Developer developer = developerService.getDeveloper();

            for (JsonNode commitNode : rootNode) {
                Commit commit = new Commit();
                commit.setHash(commitNode.get("sha").asText());
                commit.setPlatform("GitHub");

                // Commit detaylarını parse et
                JsonNode commitDetailsNode = commitNode.get("commit");
                if (commitDetailsNode != null) {
                    commit.setMessage(commitDetailsNode.get("message").asText());

                    // Commit'in patch bilgisi için URL oluştur
                    String commitDetailsUrl = String.format("https://api.github.com/repos/%s/%s/commits/%s",
                            githubOwner, githubRepo, commitNode.get("sha").asText());

                    // Patch bilgisini çek
                    String patch = fetchPatchData(commitDetailsUrl);

                    // Eğer patch boyutu 65.535 karakterden uzun ise kısalt
                    if (patch != null && patch.length() > 10000) { // MySQL TEXT alanının maksimum boyutu
                        patch = patch.substring(0, 500); // İlk 500 karakteri alın
                    }

                    commit.setPatch(patch); // Patch bilgisini set et

                    // Parse author bilgileri
                    JsonNode authorNode = commitDetailsNode.get("author");
                    if (authorNode != null) {
                        commit.setAuthorName(authorNode.get("name").asText());
                        commit.setAuthorEmail(authorNode.get("email").asText());
                        commit.setTimestamp(LocalDateTime.parse(authorNode.get("date").asText(), DateTimeFormatter.ISO_DATE_TIME));
                    }
                }

                // Geliştiriciyi set et
                commit.setDeveloper(developer);

                // Commit'i veritabanına kaydet
                commitService.saveCommit(commit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Commit detaylarından patch bilgisi al
    private String fetchPatchData(String commitDetailsUrl) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", "application/vnd.github.v3.diff"); // Patch formatında veri almak için header
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(commitDetailsUrl, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Eğer bir hata oluşursa null döndür
        }
    }

    // GitHub API URL oluştur
    private String buildGitHubApiUrl(String owner, String repo) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        String since = oneMonthAgo.format(DateTimeFormatter.ISO_DATE_TIME);
        return String.format("https://api.github.com/repos/%s/%s/commits?since=%s", owner, repo, since);
    }
}
