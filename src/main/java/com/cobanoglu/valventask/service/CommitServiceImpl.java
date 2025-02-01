package com.cobanoglu.valventask.service;

import com.cobanoglu.valventask.model.Commit;
import com.cobanoglu.valventask.model.Developer;
import com.cobanoglu.valventask.repository.CommitRepository;
import com.cobanoglu.valventask.service.CommitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommitServiceImpl implements CommitService {

    private final CommitRepository commitRepository;

    public CommitServiceImpl(CommitRepository commitRepository) {
        this.commitRepository = commitRepository;
    }

    @Override
    public List<Commit> getCommitsLastMonth() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return commitRepository.findByTimestampAfter(oneMonthAgo);
    }

    @Override
    public Commit saveCommit(Commit commit) {
        return commitRepository.save(commit);
    }

    @Override
    public Commit getCommitById(Long id) {
        return commitRepository.findById(id).orElse(null);
    }

    @Override
    public List<Commit> getCommitsByDeveloper(Developer developer) {
        return commitRepository.findByDeveloper(developer);
    }

    @Override
    public Page<Commit> getCommits(Pageable pageable) {
        return commitRepository.findAll(pageable);
    }

    @Override
    public Page<Commit> getCommitsByPlatform(String platform, Pageable pageable) {
        // Platforma göre commitleri alır
        return commitRepository.findByPlatform(platform, pageable);
    }

    @Override
    public Page<Commit> getAllCommits(Pageable pageable) {
        // Tüm commitleri alır
        return commitRepository.findAll(pageable);
    }
}
