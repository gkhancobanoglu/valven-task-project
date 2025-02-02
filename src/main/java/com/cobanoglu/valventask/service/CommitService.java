package com.cobanoglu.valventask.service;

import com.cobanoglu.valventask.model.Commit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CommitService {

    // Bir commit'i kaydeder ve döndürür
    Commit saveCommit(Commit commit);

    // ID'ye göre bir commit getirir
    Commit getCommitById(Long id);

    Page<Commit> getCommitsByPlatform(String platform, Pageable pageable);

    Page<Commit> getAllCommits(Pageable pageable);
}
