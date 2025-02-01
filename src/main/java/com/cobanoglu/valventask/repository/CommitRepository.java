package com.cobanoglu.valventask.repository;

import com.cobanoglu.valventask.model.Commit;
import com.cobanoglu.valventask.model.Developer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommitRepository extends JpaRepository<Commit, Long> {

    // Belirli bir geliştiricinin commitlerini getirir
    List<Commit> findByDeveloperId(Long developerId);

    // Belirli bir platformdaki commitleri getirir (GitHub, GitLab gibi)
    List<Commit> findByPlatform(String platform);

    // Belirli bir tarihten sonra oluşturulmuş commitleri getirir
    List<Commit> findByTimestampAfter(LocalDateTime oneMonthAgo);

    // Belirli bir geliştiricinin commitlerini getirir (Developer nesnesine göre)
    List<Commit> findByDeveloper(Developer developer);

    Page<Commit> findByPlatform(String platform, Pageable pageable);
}
