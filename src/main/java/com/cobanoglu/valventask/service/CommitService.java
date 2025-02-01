package com.cobanoglu.valventask.service;

import com.cobanoglu.valventask.model.Commit;
import com.cobanoglu.valventask.model.Developer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommitService {
    // Son bir ayda yapılan commitleri getirir
    List<Commit> getCommitsLastMonth();

    // Bir commit'i kaydeder ve döndürür
    Commit saveCommit(Commit commit);

    // ID'ye göre bir commit getirir
    Commit getCommitById(Long id);

    // Belirtilen geliştiricinin commitlerini getirir
    List<Commit> getCommitsByDeveloper(Developer developer);

    Page<Commit> getCommits(Pageable pageable);

    Page<Commit> getCommitsByPlatform(String platform, Pageable pageable);

    Page<Commit> getAllCommits(Pageable pageable);
}
