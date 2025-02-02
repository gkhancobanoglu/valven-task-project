package com.cobanoglu.valventask.controller;

import com.cobanoglu.valventask.model.Commit;
import com.cobanoglu.valventask.model.Developer;
import com.cobanoglu.valventask.service.CommitService;
import com.cobanoglu.valventask.service.DeveloperService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class DeveloperController {

    private final DeveloperService developerService;
    private final CommitService commitService;

    // Constructor ile servislerin bağımlılık enjeksiyonu yapılıyor.
    public DeveloperController(DeveloperService developerService, CommitService commitService) {
        this.developerService = developerService;
        this.commitService = commitService;
    }

    // Geliştirici bilgilerini gösteren endpoint
    @GetMapping("/developer")
    public String getDeveloperInfo(Model model) {
        // Geliştirici bilgilerini al
        Developer developer = developerService.getDeveloper();
        // Model'e geliştirici bilgilerini ekle
        model.addAttribute("developer", developer);
        // developer.html Thymeleaf şablonuna yönlendir
        return "developer";
    }

    @GetMapping("/developer/commits")
    public String getCommits(
            @RequestParam(required = false) String platform,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Page<Commit> commits;
        Pageable pageable = PageRequest.of(page, size);

        if (platform == null || platform.isEmpty()) {
            commits = commitService.getAllCommits(pageable);
        } else {
            commits = commitService.getCommitsByPlatform(platform, pageable);
        }

        model.addAttribute("commits", commits);
        model.addAttribute("platform", platform); // Seçili platform bilgisini ekle
        return "commit-list";
    }



    // Belirli bir commit'in detaylarını gösteren endpoint
    @GetMapping("/developer/commits/{id}")
    public String getCommitDetails(@PathVariable Long id, Model model) {
        // Belirtilen ID'ye sahip commit'i al
        Commit commit = commitService.getCommitById(id);
        // Eğer commit bulunamazsa commit listesi sayfasına yönlendir
        if (commit == null) {
            return "redirect:/commits";
        }

        // Model'e commit detaylarını ekle
        model.addAttribute("commit", commit);
        // commit-detail.html Thymeleaf şablonuna yönlendir
        return "commit-detail";
    }
}
